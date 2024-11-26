package karya.servers.scheduler.usecases.external

import karya.core.entities.Task
import karya.servers.scheduler.app.SchedulerManager
import karya.servers.scheduler.usecases.internal.ProcessTask
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ReceiveChannel
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class WorkerService
@Inject
constructor(
  private val processTask: ProcessTask,
) {
  companion object : Logging

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
