package karya.core.entities.requests

import karya.core.entities.Hook
import karya.core.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

/**
 * Data class representing a request to update a plan.
 *
 * @property planId The unique identifier of the plan to be updated.
 * @property periodTime The new period time for the plan (optional).
 * @property maxFailureRetry The new maximum number of retries on failure (optional).
 * @property hooks The new list of hooks to be associated with the plan (optional).
 */
@Serializable
data class UpdatePlanRequest(
  @Serializable(with = UUIDSerializer::class)
  val planId: UUID,
  val periodTime: String? = null,
  val maxFailureRetry: Int? = null,
  val hooks: List<Hook>? = null
)
