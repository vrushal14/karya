package karya.servers.scheduler.usecases.internal

import karya.core.entities.Job
import karya.core.entities.Task
import karya.core.entities.messages.ExecutorMessage
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class PushTaskToQueue
@Inject
constructor(

) {

  companion object : Logging

  suspend fun invoke(job: Job, task: Task) = ExecutorMessage(
    jobId = job.id,
    taskId = task.id,
    action = job.action,
    maxFailureRetry = job.maxFailureRetry
  ).also { logger.info { "Pushing message to queue --- $it" } }
}