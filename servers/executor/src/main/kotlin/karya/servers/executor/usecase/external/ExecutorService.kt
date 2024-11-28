package karya.servers.executor.usecase.external

import karya.core.queues.QueueClient
import karya.core.repos.RepoConnector
import karya.servers.executor.configs.ExecutorConfig
import karya.servers.executor.usecase.internal.ExecuteAction
import kotlinx.coroutines.runBlocking
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class ExecutorService
@Inject
constructor(
  private val repoConnector: RepoConnector,
  private val queueClient: QueueClient,
  private val executeAction: ExecuteAction,
  private val config: ExecutorConfig
) {

  companion object : Logging

  suspend fun start() {
    logger.info("Starting executor service...")
    queueClient.initialize()
    queueClient.consume { message -> executeAction.invoke(message) }
  }

  fun stop() = runBlocking {
    logger.info("Shutting down executor service...")
    queueClient.shutdown()
    repoConnector.shutdown()
    config.connectors.forEach { action, connector ->
      runBlocking { connector.shutdown() }
      logger.info("Shutdown connector complete for action : $action")
    }
    logger.info("Executor service shutdown complete.")
  }
}
