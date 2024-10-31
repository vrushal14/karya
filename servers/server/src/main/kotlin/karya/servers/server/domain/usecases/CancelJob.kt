package karya.servers.server.domain.usecases

import karya.core.entities.Job
import karya.core.entities.enums.JobStatus
import karya.core.exceptions.JobException.JobNotFoundException
import karya.core.exceptions.LocksException.UnableToAcquireLockException
import karya.core.locks.LocksClient
import karya.core.repos.JobsRepo
import org.apache.logging.log4j.kotlin.Logging
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class CancelJob
@Inject
constructor(
  private val jobsRepo: JobsRepo,
  private val locksClient: LocksClient
){
  companion object: Logging

  suspend fun invoke(jobId : UUID) : Job =
    if (locksClient.getLock(jobId)) {
      val job = jobsRepo.get(jobId) ?: throw JobNotFoundException(jobId)
      job.copy(
        status = JobStatus.CANCELLED,
        updatedAt = Instant.now().toEpochMilli()
      )
        .also { jobsRepo.update(it) }
        .also { logger.info("Cancelled job --- $jobId") }
        .also { locksClient.freeLock(jobId) }

    } else {
      logger.warn("Unable to acquire lock. Try again later")
      throw UnableToAcquireLockException(jobId)
    }
}