package karya.servers.server.domain.usecases.external

import karya.core.entities.Plan
import karya.core.entities.requests.UpdatePlanRequest
import karya.core.exceptions.LocksException.UnableToAcquireLockException
import karya.core.exceptions.PlanException.PlanNotFoundException
import karya.core.locks.LocksClient
import karya.core.locks.entities.LockResult
import karya.core.repos.PlansRepo
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class UpdatePlan
@Inject
constructor(
  private val locksClient: LocksClient,
  private val plansRepo: PlansRepo,
) {
  companion object : Logging

  suspend fun invoke(request: UpdatePlanRequest): Plan {
    val result = locksClient.withLock(request.planId) { updatePlanInternal(request) }
    return when (result) {
      is LockResult.Success -> result.result
      is LockResult.Failure -> {
        logger.warn("Unable to acquire lock. Try again later")
        throw UnableToAcquireLockException(request.planId)
      }
    }
  }

  private suspend fun updatePlanInternal(request: UpdatePlanRequest): Plan {
    val plan = plansRepo.get(request.planId) ?: throw PlanNotFoundException(request.planId)
    return plan
      .update(request)
      .also { plansRepo.update(it) }
      .also { logger.info("Updated plan --- ${request.planId}") }
  }
}
