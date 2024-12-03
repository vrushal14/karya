package karya.core.entities.responses

import karya.core.entities.ErrorLog
import karya.core.entities.Plan
import karya.core.entities.Task
import kotlinx.serialization.Serializable

@Serializable
data class GetSummaryResponse(
  val plan: Plan,
  val list: List<Task>,
  val errorLogs: List<ErrorLog>
)
