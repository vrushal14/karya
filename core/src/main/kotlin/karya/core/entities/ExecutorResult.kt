package karya.core.entities

import kotlinx.serialization.Serializable

/**
 * Sealed class representing the result of an executor's operation.
 */
@Serializable
sealed class ExecutorResult {

  /**
   * Data class representing a successful executor result.
   *
   * @property timestamp The timestamp when the success occurred.
   */
  @Serializable
  data class Success(
    val timestamp: Long
  ) : ExecutorResult()

  /**
   * Data class representing a failed executor result.
   *
   * @property reason The reason for the failure.
   * @property action The action that was taken that led to the failure.
   * @property timestamp The timestamp when the failure occurred.
   */
  @Serializable
  data class Failure(
    val reason: String,
    val action: Action,
    val timestamp: Long
  ) : ExecutorResult()
}
