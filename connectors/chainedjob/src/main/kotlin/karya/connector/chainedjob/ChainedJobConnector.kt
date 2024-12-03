package karya.connector.chainedjob

import karya.core.actors.Connector
import karya.core.entities.Action
import karya.core.entities.ExecutorResult
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

  override suspend fun invoke(jobId: UUID, action: Action.ChainedRequest): ExecutorResult = try {
    submitJob.invoke(action.request, parentJobId = jobId)
    ExecutorResult.Success(Instant.now().toEpochMilli())

  } catch (e: KaryaException) {
    ExecutorResult.Failure(
      reason = "Chained job failed --- error : ${e.message}",
      action = action,
      timestamp = Instant.now().toEpochMilli()
    )
  }

  override suspend fun shutdown() {
    repoConnector.shutdown()
  }
}
