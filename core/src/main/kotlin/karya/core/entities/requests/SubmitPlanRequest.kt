package karya.core.entities.requests

import karya.core.entities.Action
import karya.core.entities.Hook
import karya.core.entities.PlanType
import karya.core.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

/**
 * Data class representing the request to submit a plan.
 *
 * @property userId The unique identifier of the user submitting the plan.
 * @property description A brief description of the plan.
 * @property periodTime The time period for the plan.
 * @property planType The type of the plan.
 * @property action The action to be taken for the plan.
 * @property hooks The list of hooks associated with the plan.
 * @property maxFailureRetry The maximum number of retries allowed for failures.
 */
@Serializable
data class SubmitPlanRequest(
  @Serializable(with = UUIDSerializer::class)
  val userId: UUID,
  val description: String,
  val periodTime: String,
  val planType: PlanType,
  val action: Action,
  val hooks: List<Hook> = listOf(),
  val maxFailureRetry: Int = 3,
)
