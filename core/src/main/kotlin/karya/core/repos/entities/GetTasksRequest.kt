package karya.core.repos.entities

import karya.core.entities.enums.TaskStatus
import java.time.Duration
import java.time.Instant

data class GetTasksRequest(
  val partitionKeys: List<Int>,
  val executionTime: Instant,
  val buffer: Duration,
  val status: TaskStatus,
)
