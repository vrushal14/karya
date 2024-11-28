package karya.core.actors

import karya.core.entities.action.Action
import java.time.Instant

sealed class Result {
  data class Success(
    val timestamp: Instant
  ) : Result()

  data class Failure(
    val reason: String,
    val action: Action,
    val exception: Exception? = null,
    val timestamp: Instant
  ) : Result()
}

interface Connector<T : Action> {
  suspend fun invoke(action: T): Result
}
