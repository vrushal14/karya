package karya.core.actors

import karya.core.entities.Action
import karya.core.entities.ExecutorResult
import java.util.*

interface Connector<T : Action> {
  suspend fun invoke(jobId: UUID, action: T): ExecutorResult
  suspend fun shutdown()
}
