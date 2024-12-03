package karya.servers.scheduler.usecases.internal

import karya.core.entities.Plan
import karya.core.entities.PlanType
import karya.core.entities.enums.PlanStatus
import karya.servers.scheduler.usecases.utils.getInstanceName
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class ShouldCreateNextTask
@Inject
constructor() {

  companion object : Logging

  suspend fun invoke(plan: Plan): Boolean =
    isPlanNonTerminal(plan) && isPlanRecurring(plan.type)
      .also { logger.info("[${getInstanceName()}] : shouldCreateNextTask : $it") }

  private fun isPlanNonTerminal(plan: Plan) =
    (plan.status == PlanStatus.CREATED).or(plan.status == PlanStatus.RUNNING)

  private fun isPlanRecurring(planType: PlanType): Boolean = when (val type = planType) {
    is PlanType.Recurring -> type.isEnded().not()
    is PlanType.OneTime -> false
  }
}
