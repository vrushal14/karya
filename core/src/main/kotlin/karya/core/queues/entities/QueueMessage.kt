package karya.core.queues.entities

import karya.core.entities.Action
import karya.core.entities.Hook
import karya.core.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
sealed class QueueMessage {

  @Serializable
  data class ExecutorMessage(
    @Serializable(with = UUIDSerializer::class)
    val planId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val taskId: UUID,
    val action: Action,
    val maxFailureRetry: Int,
  ) : QueueMessage()

  @Serializable
  data class HookMessage(
    @Serializable(with = UUIDSerializer::class)
    val planId: UUID,
    val hook: Hook
  ) : QueueMessage()

}
