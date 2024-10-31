package karya.core.entities.requests

import karya.core.entities.enums.JobType
import java.net.URL
import java.util.UUID

data class SubmitJobRequest(
  val userId: UUID,
  val periodTime: String,
  val jobType: JobType,
  val executorEndpoint: URL,
  val maxFailureRetry: Int = 3
)
