package karya.core.entities.requests

import karya.core.entities.Action
import karya.core.entities.enums.JobType
import karya.core.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class SubmitJobRequest(

  @Serializable(with = UUIDSerializer::class)
  val userId: UUID,

  val periodTime: String,
  val jobType: JobType,
  val action: Action,
  val maxFailureRetry: Int = 3
)
