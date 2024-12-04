package karya.core.entities

import karya.core.entities.enums.PlanStatus
import karya.core.entities.requests.UpdatePlanRequest
import karya.core.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.*

/**
 * Data class representing a plan.
 *
 * @property id The unique identifier of the plan.
 * @property userId The unique identifier of the user associated with the plan.
 * @property description A brief description of the plan.
 * @property periodTime The period time for the plan.
 * @property type The type of the plan.
 * @property status The current status of the plan.
 * @property maxFailureRetry The maximum number of retries on failure.
 * @property action The action associated with the plan.
 * @property hook The list of hooks associated with the plan.
 * @property parentPlanId The unique identifier of the parent plan, if any.
 * @property createdAt The timestamp when the plan was created.
 * @property updatedAt The timestamp when the plan was last updated.
 */
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
  /**
   * Updates the current plan with the provided update request.
   *
   * @param request The update request containing the new values for the plan.
   * @return A new Plan instance with the updated values.
   */
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
