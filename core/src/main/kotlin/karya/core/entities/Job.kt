package karya.core.entities

import karya.core.entities.enums.JobStatus
import karya.core.entities.enums.JobType
import karya.core.entities.requests.UpdateJobRequest
import karya.core.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
data class Job(

  @Serializable(with = UUIDSerializer::class)
  val id: UUID,

  @Serializable(with = UUIDSerializer::class)
  val userId: UUID,

  val periodTime: String,
  val type: JobType,
  val status: JobStatus,
  val maxFailureRetry: Int,
  val action: Action,
  val createdAt: Long,
  val updatedAt: Long
) {
  fun update(request: UpdateJobRequest) = Job(
    id = id,
    userId = userId,
    periodTime = request.periodTime ?: periodTime,
    type = type,
    status = status,
    maxFailureRetry = request.maxFailureRetry ?: maxFailureRetry,
    action = action,
    createdAt = createdAt,
    updatedAt = Instant.now().toEpochMilli()
  )
}
