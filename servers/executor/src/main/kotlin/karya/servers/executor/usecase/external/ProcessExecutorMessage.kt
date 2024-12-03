package karya.servers.executor.usecase.external

import karya.core.entities.Job
import karya.core.entities.JobType
import karya.core.entities.enums.JobStatus
import karya.core.exceptions.JobException
import karya.core.exceptions.KaryaException
import karya.core.queues.QueueClient
import karya.core.queues.entities.QueueMessage
import karya.core.queues.entities.QueueType
import karya.core.repos.JobsRepo
import karya.servers.executor.usecase.internal.GetConnector
import karya.servers.executor.usecase.internal.ProcessTask
import karya.servers.executor.usecase.internal.TriggerHook
import org.apache.logging.log4j.kotlin.Logging
import java.util.*
import javax.inject.Inject

class ProcessExecutorMessage
@Inject
constructor(
  private val queueClient: QueueClient,
  private val jobsRepo: JobsRepo,

  private val processTask: ProcessTask,
  private val triggerHook: TriggerHook,
  private val getConnector: GetConnector
) {

  companion object : Logging

  suspend fun invoke(message: QueueMessage.ExecutorMessage) = try {
    val connector = getConnector.invoke(message.action)
    val result = connector.invoke(message.jobId, message.action)
    val job = jobsRepo.get(message.jobId) ?: throw JobException.JobNotFoundException(message.jobId)

    processTask.invoke(result, message)
    processJob(job)
    job.hook.forEach { triggerHook.invoke(job, it, result) }

  } catch (e: KaryaException) {
    logger.error(e) { "[PUSHING MESSAGE TO DLQ] Exception while executing action --- ${e.message}" }
    queueClient.push(message, QueueType.DEAD_LETTER)
  }

  private suspend fun processJob(job: Job) {
    when (val type = job.type) {
      is JobType.Recurring -> if (type.isEnded()) markJobCompleted(job.id) else return
      is JobType.OneTime -> markJobCompleted(job.id)
    }
    logger.info("[JOB COMPLETED] --- jobId : $job.id")
  }

  private suspend fun markJobCompleted(jobId: UUID) =
    jobsRepo.updateStatus(jobId, JobStatus.COMPLETED)
}
