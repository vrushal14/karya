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

/**
 * Scheduler fetcher responsible for starting and stopping the task fetching process.
 *
 * @property fetcherService The service used to fetch tasks.
 * @property repoConnector The repository connector for managing database connections.
 * @property config The configuration for the scheduler.
 * @constructor Creates an instance of [SchedulerFetcher] with the specified dependencies.
 */
class SchedulerFetcher
@Inject
constructor(
  private val fetcherService: FetcherService,
  private val repoConnector: RepoConnector,
  config: SchedulerConfig
) {
  companion object : Logging

  private val taskChannel = Channel<Task>(capacity = config.channelCapacity)

  /**
   * Provides a receive-only channel for consumers to read tasks.
   */
  val taskReadChannel: ReceiveChannel<Task> get() = taskChannel

  /**
   * Starts the fetcher service to begin fetching tasks.
   */
  suspend fun start() {
    logger.info { "Starting fetcher..." }
    withNamedContext("scheduler-karya-fetcher") { fetcherService.invoke(taskChannel) }
    logger.info { "Fetcher started." }
  }

  /**
   * Stops the fetcher service and shuts down the repository connector.
   */
  suspend fun stop() {
    logger.info { "Shutting down fetcher..." }
    repoConnector.shutdown()
    taskChannel.close()
    logger.info { "Fetcher shutdown complete." }
  }
}
