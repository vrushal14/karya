package karya.core.entities.messages

import karya.core.entities.Action
import java.util.UUID

data class ExecutorMessage(
  val jobId : UUID,
  val taskId : UUID,
  val action : Action,
  val maxFailureRetry : Int
)
