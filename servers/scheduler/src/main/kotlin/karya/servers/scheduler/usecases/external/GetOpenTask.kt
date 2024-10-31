package karya.servers.scheduler.usecases.external

import karya.core.entities.enums.TaskStatus
import karya.core.repos.TasksRepo
import karya.core.repos.entities.GetTasksRequest
import karya.servers.scheduler.configs.SchedulerConfig
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

class GetOpenTask
@Inject
constructor(
  private val tasksRepo : TasksRepo,
  private val config: SchedulerConfig,
) {

  suspend fun invoke() = GetTasksRequest(
    partitionKeys = config.partitions,
    executionTime = Instant.now(),
    buffer = Duration.ofMillis(config.executionBufferInMilli),
    status = TaskStatus.CREATED
  )
    .let { tasksRepo.get(it) }
}