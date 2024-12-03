package karya.core.entities

import kotlinx.serialization.Serializable

@Serializable
sealed class ExecutorResult {

  @Serializable
  data class Success(
    val timestamp: Long
  ) : ExecutorResult()

  @Serializable
  data class Failure(
    val reason: String,
    val action: Action,
    val timestamp: Long
  ) : ExecutorResult()
}
