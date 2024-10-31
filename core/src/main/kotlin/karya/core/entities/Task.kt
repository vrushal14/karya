package karya.core.entities

import karya.core.entities.enums.TaskStatus
import java.net.URL
import java.util.UUID

data class Task(
  val id: UUID,
  val jobId: UUID,
  val partitionKey: Int,
  val status: TaskStatus,
  val createdAt: Long,
  val executedAt: Long?,
  val nextExecutionAt: Long?
)
