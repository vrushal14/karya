package karya.servers.executor.usecase.external

import karya.core.entities.Plan
import karya.core.entities.PlanType
import karya.core.entities.enums.PlanStatus
import karya.core.exceptions.PlanException
import karya.core.exceptions.KaryaException
import karya.core.queues.QueueClient
import karya.core.queues.entities.QueueMessage
import karya.core.queues.entities.QueueType
import karya.core.repos.PlansRepo
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
  private val plansRepo: PlansRepo,

  private val processTask: ProcessTask,
  private val triggerHook: TriggerHook,
  private val getConnector: GetConnector
) {

  companion object : Logging

  suspend fun invoke(message: QueueMessage.ExecutorMessage) = try {
    val connector = getConnector.invoke(message.action)
    val result = connector.invoke(message.planId, message.action)
    val plan = plansRepo.get(message.planId) ?: throw PlanException.PlanNotFoundException(message.planId)

    processTask.invoke(result, message)
    processPlan(plan)
    plan.hook.forEach { triggerHook.invoke(plan, it, result) }

  } catch (e: KaryaException) {
    logger.error(e) { "[PUSHING MESSAGE TO DLQ] Exception while executing action --- ${e.message}" }
    queueClient.push(message, QueueType.DEAD_LETTER)
  }

  private suspend fun processPlan(plan: Plan) {
    when (val type = plan.type) {
      is PlanType.Recurring -> if (type.isEnded()) markPlanCompleted(plan.id) else return
      is PlanType.OneTime -> markPlanCompleted(plan.id)
    }
    logger.info("[PLAN COMPLETED] --- planId : $plan.id")
  }

  private suspend fun markPlanCompleted(planId: UUID) =
    plansRepo.updateStatus(planId, PlanStatus.COMPLETED)
}
