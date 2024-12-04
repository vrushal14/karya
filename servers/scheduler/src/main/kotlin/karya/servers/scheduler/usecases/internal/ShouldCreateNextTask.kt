package karya.servers.scheduler.usecases.internal

import karya.core.entities.Plan
import karya.core.entities.PlanType
import karya.core.entities.enums.PlanStatus
import karya.servers.scheduler.usecases.utils.getInstanceName
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

/**
 * Use case to determine if the next task should be created for a given plan.
 *
 * @constructor Creates an instance of [ShouldCreateNextTask].
 */
class ShouldCreateNextTask
@Inject
constructor() {

  companion object : Logging

  /**
   * Determines if the next task should be created based on the plan's status and type.
   *
   * @param plan The plan to be evaluated.
   * @return `true` if the next task should be created, `false` otherwise.
   */
  suspend fun invoke(plan: Plan): Boolean =
    isPlanNonTerminal(plan) && isPlanRecurring(plan.type)
      .also { logger.info("[${getInstanceName()}] : shouldCreateNextTask : $it") }

  /**
   * Checks if the plan is in a non-terminal state (created or running).
   *
   * @param plan The plan to be checked.
   * @return `true` if the plan is in a non-terminal state, `false` otherwise.
   */
  private fun isPlanNonTerminal(plan: Plan) =
    (plan.status == PlanStatus.CREATED).or(plan.status == PlanStatus.RUNNING)

  /**
   * Checks if the plan type is recurring and has not ended.
   *
   * @param planType The type of the plan to be checked.
   * @return `true` if the plan type is recurring and has not ended, `false` otherwise.
   */
  private fun isPlanRecurring(planType: PlanType): Boolean = when (val type = planType) {
    is PlanType.Recurring -> type.isEnded().not()
    is PlanType.OneTime -> false
  }
}
