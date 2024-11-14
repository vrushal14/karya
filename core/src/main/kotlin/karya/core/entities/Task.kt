package karya.core.entities

import karya.core.entities.enums.TaskStatus
import karya.core.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Task(

  @Serializable(with = UUIDSerializer::class)
  val id: UUID,
  @Serializable(with = UUIDSerializer::class)
  val jobId: UUID,

  val partitionKey: Int,
  val status: TaskStatus,
  val createdAt: Long,
  val executedAt: Long?,
  val nextExecutionAt: Long?
)
