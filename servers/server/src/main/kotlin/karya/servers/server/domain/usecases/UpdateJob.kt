package karya.servers.server.domain.usecases

import karya.core.entities.Job
import karya.core.entities.requests.UpdateJobRequest
import karya.core.exceptions.JobException.JobNotFoundException
import karya.core.exceptions.LocksException.UnableToAcquireLockException
import karya.core.locks.LocksClient
import karya.core.repos.JobsRepo
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class UpdateJob
@Inject
constructor(
  private val locksClient: LocksClient,
  private val jobsRepo: JobsRepo
){
  companion object : Logging

  suspend fun invoke(request: UpdateJobRequest) : Job =
    if(locksClient.getLock(request.jobId)) {
      val job = jobsRepo.get(request.jobId) ?: throw JobNotFoundException(request.jobId)
      job.update(request)
        .also { jobsRepo.update(it) }
        .also { logger.info("Updated job --- ${request.jobId}") }
        .also { locksClient.freeLock(it.id) }

    } else {
      logger.warn("Unable to acquire lock. Try again later")
      throw UnableToAcquireLockException(request.jobId)
    }
}