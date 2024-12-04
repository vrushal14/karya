package karya.core.entities

import karya.core.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

/**
 * Sealed class representing different types of error logs.
 */
@Serializable
sealed class ErrorLogType {

  /**
   * Object representing a hook error log. This will be generated when a hook fails.
   */
  @Serializable
  object HookErrorLog : ErrorLogType()

  /**
   * Data class representing an executor error log. This will be generated when an executor fails.
   *
   * @property taskId The unique identifier of the task associated with the error.
   */
  @Serializable
  data class ExecutorErrorLog(
    @Serializable(with = UUIDSerializer::class)
    val taskId: UUID
  ) : ErrorLogType()
}
