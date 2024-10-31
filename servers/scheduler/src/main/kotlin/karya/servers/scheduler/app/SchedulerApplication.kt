package karya.servers.scheduler.app

import karya.core.connectors.RepoConnector
import karya.servers.scheduler.configs.SchedulerConfig
import karya.servers.scheduler.usecases.PollerService
import kotlinx.coroutines.runBlocking
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class SchedulerApplication
@Inject
constructor(
  private val pollerService: PollerService,
  private val repoConnector: RepoConnector,
  private val config: SchedulerConfig
) {

  companion object : Logging

  fun start() {
    logger.info { "[${config.getName()}] --- Starting poller" }
    pollerService.start()
  }

  fun stop() = runBlocking {
    logger.info { "[${config.getName()}] --- Shutting down poller" }
    pollerService.stop()
    repoConnector.shutdown()
  }

}