package karya.servers.server.domain.usecases

import karya.core.entities.Job
import karya.core.entities.requests.UpdateJobRequest
import karya.core.exceptions.JobException.JobNotFoundException
import karya.core.exceptions.LocksException.UnableToAcquireLockException
import karya.core.locks.LocksClient
import karya.core.locks.entities.LockResult
import karya.core.repos.JobsRepo
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class UpdateJob
@Inject
constructor(
  private val locksClient: LocksClient,
  private val jobsRepo: JobsRepo,
) {
  companion object : Logging

  suspend fun invoke(request: UpdateJobRequest): Job {
    val result = locksClient.withLock(request.jobId) { updateJobInternal(request) }
    return when (result) {
      is LockResult.Success -> result.result
      is LockResult.Failure -> {
        logger.warn("Unable to acquire lock. Try again later")
        throw UnableToAcquireLockException(request.jobId)
      }
    }
  }

  private suspend fun updateJobInternal(request: UpdateJobRequest): Job {
    val job = jobsRepo.get(request.jobId) ?: throw JobNotFoundException(request.jobId)
    return job
      .update(request)
      .also { jobsRepo.update(it) }
      .also { logger.info("Updated job --- ${request.jobId}") }
  }
}
