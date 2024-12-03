package karya.core.entities

import karya.core.entities.enums.PlanStatus
import karya.core.entities.requests.UpdatePlanRequest
import karya.core.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.*

@Serializable
data class Plan(
  @Serializable(with = UUIDSerializer::class)
  val id: UUID,
  @Serializable(with = UUIDSerializer::class)
  val userId: UUID,
  val description: String,
  val periodTime: String,
  val type: PlanType,
  val status: PlanStatus,
  val maxFailureRetry: Int,
  val action: Action,
  val hook: List<Hook>,
  @Serializable(with = UUIDSerializer::class)
  val parentPlanId: UUID?,
  val createdAt: Long,
  val updatedAt: Long,
) {
  fun update(request: UpdatePlanRequest) =
    Plan(
      id = id,
      userId = userId,
      description = description,
      periodTime = request.periodTime ?: periodTime,
      type = type,
      status = status,
      maxFailureRetry = request.maxFailureRetry ?: maxFailureRetry,
      action = action,
      hook = request.hooks ?: hook,
      parentPlanId = parentPlanId,
      createdAt = createdAt,
      updatedAt = Instant.now().toEpochMilli(),
    )
}
