package karya.core.exceptions

/**
 * Sealed class representing exceptions related to error logs.
 */
sealed class ErrorLogsException : KaryaException() {

  /**
   * Exception thrown when a task ID is not found for an error log type.
   *
   * @property message The error message.
   */
  data class TaskIdNotFoundException(
    override val message: String = "Task ID not found for ErrorLog type --- Executor"
  ) : ErrorLogsException()

  /**
   * Exception thrown when an invalid error log type is encountered.
   *
   * @property errorLogType The invalid error log type.
   * @property message The error message.
   */
  data class InvalidErrorLogTypeException(
    private val errorLogType: Int,
    override val message: String = "ErrorLogType [$errorLogType] not found"
  ) : ErrorLogsException()
}
