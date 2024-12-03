package karya.servers.server.domain.usecases.external

import karya.core.entities.Plan
import karya.core.entities.enums.PlanStatus
import karya.core.exceptions.PlanException.PlanNotFoundException
import karya.core.exceptions.LocksException.UnableToAcquireLockException
import karya.core.locks.LocksClient
import karya.core.locks.entities.LockResult
import karya.core.repos.PlansRepo
import org.apache.logging.log4j.kotlin.Logging
import java.time.Instant
import java.util.*
import javax.inject.Inject

class CancelPlan
@Inject
constructor(
  private val plansRepo: PlansRepo,
  private val locksClient: LocksClient,
) {
  companion object : Logging

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

  private suspend fun updatePlanStatus(planId: UUID): Plan {
    val plan = plansRepo.get(planId) ?: throw PlanNotFoundException(planId)
    return plan.copy(
      status = PlanStatus.CANCELLED,
      updatedAt = Instant.now().toEpochMilli(),
    ).also { plansRepo.update(it) }
      .also { logger.info("Cancelled plan --- $planId") }
      .also { cancelChildPlansIfAny(planId) }
  }

  private suspend fun cancelChildPlansIfAny(planId: UUID) {
    val childPlans = plansRepo.getChildPlanIds(planId)
    childPlans.forEach {
      logger.info("[CANCELLING CHILD PLAN] --- planId : $planId")
      invoke(it)
    }
  }
}
