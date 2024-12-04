package karya.connector.chainedplan

import karya.core.actors.Connector
import karya.core.entities.Action
import karya.core.entities.ExecutorResult
import karya.core.exceptions.KaryaException
import karya.core.repos.RepoConnector
import karya.servers.server.domain.usecases.external.SubmitPlan
import java.time.Instant
import java.util.*
import javax.inject.Inject

/**
 * Connector implementation for handling chained plans.
 *
 * @property submitPlan Use case for submitting a plan.
 * @property repoConnector Repository connector for managing repository operations.
 */
class ChainedPlanConnector
@Inject
constructor(
  private val submitPlan: SubmitPlan,
  private val repoConnector: RepoConnector
) : Connector<Action.ChainedRequest> {

  /**
   * Invokes the chained plan with the given plan ID and action.
   *
   * @param planId The ID of the plan.
   * @param action The action to be performed.
   * @return The result of the execution.
   */
  override suspend fun invoke(planId: UUID, action: Action.ChainedRequest): ExecutorResult = try {
    submitPlan.invoke(action.request, parentPlanId = planId)
    ExecutorResult.Success(Instant.now().toEpochMilli())

  } catch (e: KaryaException) {
    ExecutorResult.Failure(
      reason = "Chained plan failed --- error : ${e.message}",
      action = action,
      timestamp = Instant.now().toEpochMilli()
    )
  }

  /**
   * Shuts down the connector and releases any resources.
   */
  override suspend fun shutdown() {
    repoConnector.shutdown()
  }
}
