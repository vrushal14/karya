package karya.core.entities.requests

import java.net.URL
import java.util.UUID

data class UpdateJobRequest(
  val jobId : UUID,
  val periodTime : String? = null,
  val executorEndpoint : URL? = null,
  val maxFailureRetry : Int? = null
)
