package karya.connector.chainedjob

import karya.core.actors.Connector
import karya.core.actors.Result
import karya.core.entities.action.Action
import karya.core.exceptions.KaryaException
import karya.servers.server.domain.usecases.SubmitJob
import java.time.Instant
import javax.inject.Inject

class ChainedJobConnector
@Inject
constructor(
  private val submitJob: SubmitJob
) : Connector<Action.ChainedRequest> {

  override suspend fun invoke(action: Action.ChainedRequest): Result = try {
    submitJob.invoke(action.request)
    Result.Success(Instant.now())

  } catch (e: KaryaException) {
    Result.Failure(
      reason = "Chained job failed --- error : ${e.message}",
      action = action,
      exception = e,
      timestamp = Instant.now()
    )
  }
}
