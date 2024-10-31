package karya.core.entities

import karya.core.entities.enums.JobStatus
import karya.core.entities.enums.JobType
import karya.core.entities.requests.UpdateJobRequest
import java.net.URL
import java.time.Instant
import java.util.UUID

data class Job(
  val id: UUID,
  val userId: UUID,
  val periodTime: String,
  val type: JobType,
  val status: JobStatus,
  val maxFailureRetry: Int,
  val executorEndpoint: URL,
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
    executorEndpoint = request.executorEndpoint ?: executorEndpoint,
    createdAt = createdAt,
    updatedAt = Instant.now().toEpochMilli()
  )
}
