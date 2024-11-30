package karya.connector.chainedjob

import karya.core.actors.Connector
import karya.core.actors.Result
import karya.core.entities.action.Action
import karya.core.exceptions.KaryaException
import karya.core.repos.RepoConnector
import karya.servers.server.domain.usecases.external.SubmitJob
import java.time.Instant
import java.util.*
import javax.inject.Inject

class ChainedJobConnector
@Inject
constructor(
  private val submitJob: SubmitJob,
  private val repoConnector: RepoConnector
) : Connector<Action.ChainedRequest> {

  override suspend fun invoke(jobId: UUID, action: Action.ChainedRequest): Result = try {
    submitJob.invoke(action.request, parentJobId = jobId)
    Result.Success(Instant.now())

  } catch (e: KaryaException) {
    Result.Failure(
      reason = "Chained job failed --- error : ${e.message}",
      action = action,
      exception = e,
      timestamp = Instant.now()
    )
  }

  override suspend fun shutdown() {
    repoConnector.shutdown()
  }
}
