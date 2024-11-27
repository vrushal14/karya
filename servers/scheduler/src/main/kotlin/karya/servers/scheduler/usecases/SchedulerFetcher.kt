package karya.servers.scheduler.usecases

import karya.core.entities.Task
import karya.core.repos.RepoConnector
import karya.servers.scheduler.configs.SchedulerConfig
import karya.servers.scheduler.usecases.external.FetcherService
import karya.servers.scheduler.usecases.utils.withNamedContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class SchedulerFetcher
@Inject
constructor(
  private val fetcherService: FetcherService,
  private val repoConnector: RepoConnector,
  config: SchedulerConfig
) {
  companion object : Logging

  private val taskChannel = Channel<Task>(capacity = config.channelCapacity)
  val taskReadChannel: ReceiveChannel<Task> get() = taskChannel // receive only channel for consumers

  suspend fun start() {
    logger.info { "Starting fetcher..." }
    withNamedContext("scheduler-karya-fetcher") { fetcherService.invoke(taskChannel) }
    logger.info { "Fetcher started." }
  }

  suspend fun stop() {
    logger.info { "Shutting down fetcher..." }
    repoConnector.shutdown()
    taskChannel.close()
    logger.info { "Fetcher shutdown complete." }
  }
}
