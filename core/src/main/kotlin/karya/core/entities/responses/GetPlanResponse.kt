package karya.core.entities.responses

import karya.core.entities.Plan
import karya.core.entities.Task
import kotlinx.serialization.Serializable

/**
 * Data class representing the response for getting a plan.
 *
 * @property plan The plan associated with the response.
 * @property latestTask The latest/last task the plan will be executing.
 */
@Serializable
data class GetPlanResponse(
  val plan: Plan,
  val latestTask: Task,
)
