package karya.servers.server.domain.usecases.external

import karya.core.entities.Plan
import karya.core.entities.enums.PlanStatus
import karya.core.exceptions.LocksException.UnableToAcquireLockException
import karya.core.exceptions.PlanException.PlanNotFoundException
import karya.core.locks.LocksClient
import karya.core.locks.entities.LockResult
import karya.core.repos.PlansRepo
import org.apache.logging.log4j.kotlin.Logging
import java.time.Instant
import java.util.*
import javax.inject.Inject

/**
 * Use case for canceling a plan.
 *
 * @property plansRepo The repository for accessing plans.
 * @property locksClient The client for managing locks.
 */
class CancelPlan
@Inject
constructor(
  private val plansRepo: PlansRepo,
  private val locksClient: LocksClient,
) {
  companion object : Logging

  /**
   * Invokes the cancel plan use case.
   *
   * @param planId The ID of the plan to cancel.
   * @return The canceled plan.
   * @throws UnableToAcquireLockException If unable to acquire a lock for the plan.
   */
  suspend fun invoke(planId: UUID): Plan {
    val result = locksClient.withLock(planId) { updatePlanStatus(planId) }
    return when (result) {
      is LockResult.Success -> result.result
      is LockResult.Failure -> {
        logger.warn("Unable to acquire lock. Try again later")
        throw UnableToAcquireLockException(planId)
      }
    }
  }

  /**
   * Updates the status of the plan to canceled.
   *
   * @param planId The ID of the plan to update.
   * @return The updated plan with canceled status.
   * @throws PlanNotFoundException If the plan is not found.
   */
  private suspend fun updatePlanStatus(planId: UUID): Plan {
    val plan = plansRepo.get(planId) ?: throw PlanNotFoundException(planId)
    return plan.copy(
      status = PlanStatus.CANCELLED,
      updatedAt = Instant.now().toEpochMilli(),
    ).also { plansRepo.update(it) }
      .also { logger.info("Cancelled plan --- $planId") }
      .also { cancelChildPlansIfAny(planId) }
  }

  /**
   * Cancels any child plans of the specified plan.
   *
   * @param planId The ID of the parent plan.
   */
  private suspend fun cancelChildPlansIfAny(planId: UUID) {
    val childPlans = plansRepo.getChildPlanIds(planId)
    childPlans.forEach {
      logger.info("[CANCELLING CHILD PLAN] --- planId : $planId")
      invoke(it)
    }
  }
}
