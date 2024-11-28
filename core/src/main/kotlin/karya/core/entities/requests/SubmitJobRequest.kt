package karya.core.entities.requests

import karya.core.entities.action.Action
import karya.core.entities.enums.JobType
import karya.core.exceptions.JobException.InvalidChainedRequestException
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
) {
  init {
    if((action is Action.ChainedRequest) && (jobType == JobType.RECURRING))
      throw InvalidChainedRequestException()
  }
}
