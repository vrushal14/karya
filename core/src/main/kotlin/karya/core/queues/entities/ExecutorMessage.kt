package karya.core.queues.entities

import karya.core.entities.action.Action
import karya.core.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ExecutorMessage(
	@Serializable(with = UUIDSerializer::class)
	val jobId: UUID,
	@Serializable(with = UUIDSerializer::class)
	val taskId: UUID,
	val action: Action,
	val maxFailureRetry: Int,
)
