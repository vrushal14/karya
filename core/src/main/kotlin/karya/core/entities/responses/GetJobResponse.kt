package karya.core.entities.responses

import karya.core.entities.Job
import karya.core.entities.Task
import kotlinx.serialization.Serializable

@Serializable
data class GetJobResponse(
  val job: Job,
  val latestTask: Task,
)
