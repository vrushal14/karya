package karya.servers.scheduler.usecases.external

import karya.core.entities.Task
import karya.core.entities.enums.TaskStatus
import karya.core.repos.TasksRepo
import karya.core.repos.entities.GetTasksRequest
import karya.servers.scheduler.app.SchedulerManager
import karya.servers.scheduler.configs.SchedulerConfig
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import org.apache.logging.log4j.kotlin.Logging
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

class FetcherService
@Inject
constructor(
  private val tasksRepo: TasksRepo,
  private val config: SchedulerConfig,
) {
  companion object : Logging

  suspend fun invoke(taskChannel: Channel<Task>) {
    while (true) {
      if (SchedulerManager.isStopped.get()) return
      val task = getOpenTask()
      if (task != null) {
        taskChannel.send(task)
      } else {
        logger.info("Poller fetched 0 tasks...")
      }
      delay(config.pollFrequency)
    }
  }

  private suspend fun getOpenTask(): Task? =
    GetTasksRequest(
      partitionKeys = config.partitions,
      executionTime = Instant.now(),
      buffer = Duration.ofMillis(config.executionBufferInMilli),
      status = TaskStatus.CREATED,
    ).let { tasksRepo.get(it) }
      ?.also { tasksRepo.updateStatus(it.id, TaskStatus.PROCESSING) }
}
