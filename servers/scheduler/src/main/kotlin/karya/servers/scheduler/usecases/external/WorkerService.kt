package karya.servers.scheduler.usecases.external

import karya.core.entities.Task
import karya.servers.scheduler.app.SchedulerManager
import karya.servers.scheduler.usecases.internal.ProcessTask
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ReceiveChannel
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

/**
 * Service class responsible for processing tasks from a receive channel.
 *
 * @property processTask The use case for processing individual tasks.
 * @constructor Creates an instance of [WorkerService] with the specified dependencies.
 */
class WorkerService
@Inject
constructor(
  private val processTask: ProcessTask,
) {
  companion object : Logging

  /**
   * Invokes the worker service to process tasks from the given channel.
   *
   * @param channel The receive channel from which tasks are consumed.
   * @throws Exception If an unexpected error occurs during task processing.
   */
  suspend fun invoke(channel: ReceiveChannel<Task>) {
    try {
      if (SchedulerManager.isStopped.get()) return
      for (task in channel) processTask.invoke(task)

    } catch (e: ClosedReceiveChannelException) {
      logger.info { "Task channel closed. Shutting down worker." }

    } catch (e: Exception) {
      logger.error(e) { "Unexpected error in worker processing." }
      throw e
    }
  }
}
