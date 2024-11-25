package karya.servers.scheduler.usecases

import karya.core.repos.RepoConnector
import karya.servers.scheduler.usecases.external.FetcherService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class SchedulerFetcher
@Inject
constructor(
  private val fetcherService: FetcherService,
  private val repoConnector: RepoConnector,
) {
  companion object : Logging

  private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

  fun provideChannel() = fetcherService.taskReadChannel

  fun start() {
    logger.info { "Starting fetcher..." }
    scope.launch {
      setFetcherName()
      fetcherService.start()
    }
    logger.info("Fetcher started.")
  }

  fun stop() =
    runBlocking {
      logger.info { "Shutting down fetcher..." }
      repoConnector.shutdown()
      fetcherService.stop()
      scope.cancel()
      logger.info("Fetcher shutdown complete.")
    }

  private fun setFetcherName(): String {
    val name = "scheduler-karya-fetcher"
    Thread.currentThread().name = name
    return name
  }
}
