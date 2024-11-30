package karya.core.queues.entities

import karya.core.entities.Action
import karya.core.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class ExecutorMessage(
  @Serializable(with = UUIDSerializer::class)
  val jobId: UUID,
  @Serializable(with = UUIDSerializer::class)
  val taskId: UUID,
  val action: Action,
  val maxFailureRetry: Int,
)
