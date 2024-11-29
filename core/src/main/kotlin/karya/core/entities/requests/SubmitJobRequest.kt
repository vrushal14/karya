package karya.core.entities.requests

import karya.core.entities.JobType
import karya.core.entities.action.Action
import karya.core.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class SubmitJobRequest(
  @Serializable(with = UUIDSerializer::class)
  val userId: UUID,
  val description: String,
  val periodTime: String,
  val jobType: JobType,
  val action: Action,
  val maxFailureRetry: Int = 3,
)
