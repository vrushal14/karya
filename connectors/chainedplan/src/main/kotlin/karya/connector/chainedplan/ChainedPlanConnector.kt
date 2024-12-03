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

class ChainedPlanConnector
@Inject
constructor(
  private val submitPlan: SubmitPlan,
  private val repoConnector: RepoConnector
) : Connector<Action.ChainedRequest> {

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

  override suspend fun shutdown() {
    repoConnector.shutdown()
  }
}
