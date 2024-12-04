package karya.core.entities

import karya.core.entities.enums.TaskStatus
import karya.core.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

/**
 * Data class representing a task.
 *
 * @property id The unique identifier of the task.
 * @property planId The unique identifier of the plan associated with the task.
 * @property partitionKey The partition key for the task. This is the partition that the task will be stored in.
 * @property status The current status of the task.
 * @property createdAt The timestamp when the task was created.
 * @property executedAt The timestamp when the task was executed (optional).
 * @property nextExecutionAt The timestamp for the next execution of the task (optional).
 */
@Serializable
data class Task(
  @Serializable(with = UUIDSerializer::class)
  val id: UUID,
  @Serializable(with = UUIDSerializer::class)
  val planId: UUID,
  val partitionKey: Int,
  val status: TaskStatus,
  val createdAt: Long,
  val executedAt: Long?,
  val nextExecutionAt: Long?,
)
