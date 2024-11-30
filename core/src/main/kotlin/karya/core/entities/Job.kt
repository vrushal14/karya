package karya.core.entities

import karya.core.entities.action.Action
import karya.core.entities.enums.JobStatus
import karya.core.entities.requests.UpdateJobRequest
import karya.core.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.*

@Serializable
data class Job(
  @Serializable(with = UUIDSerializer::class)
  val id: UUID,
  @Serializable(with = UUIDSerializer::class)
  val userId: UUID,
  val description: String,
  val periodTime: String,
  val type: JobType,
  val status: JobStatus,
  val maxFailureRetry: Int,
  val action: Action,
  @Serializable(with = UUIDSerializer::class)
  val parentJobId: UUID?,
  val createdAt: Long,
  val updatedAt: Long,
) {
  fun update(request: UpdateJobRequest) =
    Job(
      id = id,
      userId = userId,
      description = description,
      periodTime = request.periodTime ?: periodTime,
      type = type,
      status = status,
      maxFailureRetry = request.maxFailureRetry ?: maxFailureRetry,
      action = action,
      parentJobId = parentJobId,
      createdAt = createdAt,
      updatedAt = Instant.now().toEpochMilli(),
    )
}
