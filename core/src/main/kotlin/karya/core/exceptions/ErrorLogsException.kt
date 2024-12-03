package karya.core.exceptions

sealed class ErrorLogsException : KaryaException() {

  data class TaskIdNotFoundException(
    override val message: String = "Task ID not found for ErrorLog type --- Executor"
  ) : ErrorLogsException()

  data class InvalidErrorLogTypeException(
    private val errorLogType: Int,
    override val message: String = "ErrorLogType [$errorLogType] not found"
  ) : ErrorLogsException()
}
