package karya.servers.executor.usecase.internal

import karya.core.entities.ExecutorResult
import karya.core.entities.ExecutorResult.Failure
import karya.core.entities.ExecutorResult.Success
import karya.core.entities.Hook
import karya.core.entities.Plan
import karya.core.entities.PlanType
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

  suspend fun invoke(plan: Plan, hook: Hook, result: ExecutorResult) {
    when (result) {
      is Success -> if (shouldTriggerOnCompletion(plan.type, hook.trigger)) pushToHookQueue(plan.id, hook)
      is Failure -> if (shouldTriggerOnFailure(hook.trigger)) pushToHookQueue(plan.id, hook)
    }
  }

  private fun shouldTriggerOnCompletion(planType: PlanType, trigger: Trigger) = when (planType) {
    is PlanType.OneTime -> trigger == ON_COMPLETION
    is PlanType.Recurring -> planType.isEnded().and(trigger == ON_COMPLETION)
  }.also { logger.info("[HOOK] --- shouldTriggerOnCompletion : $it") }

  private fun shouldTriggerOnFailure(trigger: Trigger) =
    (trigger == ON_FAILURE).also { logger.info("[HOOK] --- shouldTriggerOnFailure : $it") }

  private suspend fun pushToHookQueue(planId: UUID, hook: Hook) =
    QueueMessage.HookMessage(planId = planId, hook = hook).let { queueClient.push(it, QueueType.HOOK) }

}
