package karya.servers.scheduler.usecases.internal

import karya.core.entities.Job
import karya.core.entities.JobType
import karya.core.entities.enums.JobStatus
import karya.servers.scheduler.usecases.utils.getInstanceName
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class ShouldCreateNextTask
@Inject
constructor() {

  companion object : Logging

  suspend fun invoke(job: Job): Boolean =
    isJobNonTerminal(job) && isJobRecurring(job.type)
      .also { logger.info("[${getInstanceName()}] : shouldCreateNextTask : $it") }

  private fun isJobNonTerminal(job: Job) =
    (job.status == JobStatus.CREATED).or(job.status == JobStatus.RUNNING)

  private fun isJobRecurring(jobType: JobType): Boolean = when (val type = jobType) {
    is JobType.Recurring -> type.isEnded().not()
    is JobType.OneTime -> false
  }
}
