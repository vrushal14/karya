package karya.core.entities.responses

import karya.core.entities.Plan
import karya.core.entities.Task
import kotlinx.serialization.Serializable

@Serializable
data class GetPlanResponse(
  val plan: Plan,
  val latestTask: Task,
)
