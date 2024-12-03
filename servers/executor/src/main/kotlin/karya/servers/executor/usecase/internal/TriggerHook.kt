package karya.servers.executor.usecase.internal

import karya.core.entities.ExecutorResult
import karya.core.entities.Hook
import karya.core.entities.Job
import karya.core.entities.JobType
import karya.core.entities.enums.Trigger
import karya.core.entities.enums.Trigger.ON_COMPLETION
import karya.core.entities.enums.Trigger.ON_FAILURE
import karya.core.queues.QueueClient
import karya.core.queues.entities.QueueMessage
import karya.core.queues.entities.QueueType
import org.apache.logging.log4j.kotlin.Logging
import java.util.*
import javax.inject.Inject

class TriggerHook
@Inject
constructor(
  private val queueClient: QueueClient
) {

  companion object : Logging

  suspend fun invoke(job: Job, hook: Hook, result: ExecutorResult) {
    when (result) {
      is ExecutorResult.Success -> if (shouldTriggerOnCompletion(job.type, hook.trigger)) pushToHookQueue(job.id, hook)
      is ExecutorResult.Failure -> if (shouldTriggerOnFailure(hook.trigger)) pushToHookQueue(job.id, hook)
    }
  }

  private fun shouldTriggerOnCompletion(jobType: JobType, trigger: Trigger) = when (jobType) {
    is JobType.OneTime -> trigger == ON_COMPLETION
    is JobType.Recurring -> jobType.isEnded().and(trigger == ON_COMPLETION)
  }.also { logger.info("[HOOK] --- shouldTriggerOnCompletion : $it") }

  private fun shouldTriggerOnFailure(trigger: Trigger) =
    (trigger == ON_FAILURE).also { logger.info("[HOOK] --- shouldTriggerOnFailure : $it") }

  private suspend fun pushToHookQueue(jobId: UUID, hook: Hook) =
    QueueMessage.HookMessage(jobId = jobId, hook = hook).let { queueClient.push(it, QueueType.HOOK) }

}
