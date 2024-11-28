package karya.servers.executor.usecase.external

import karya.core.queues.QueueClient
import karya.core.repos.RepoConnector
import karya.servers.executor.usecase.internal.ExecuteAction
import kotlinx.coroutines.runBlocking
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class ExecutorService
@Inject
constructor(
  private val repoConnector: RepoConnector,
  private val queueClient: QueueClient,
  private val executeAction: ExecuteAction
){

  companion object : Logging

  suspend fun start() {
   queueClient.consume { message -> executeAction.invoke(message) }
  }

  fun stop() = runBlocking {
    queueClient.shutdown()
    repoConnector.shutdown()
  }
}
