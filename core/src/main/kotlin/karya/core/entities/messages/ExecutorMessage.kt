package karya.core.entities.messages

import java.net.URL
import java.util.UUID

data class ExecutorMessage(
  val jobId : UUID,
  val taskId : UUID,
  val executorEndpoint : URL,
  val maxFailureRetry : Int
)
