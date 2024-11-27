package karya.core.exceptions

import java.util.*

sealed class TaskException : KaryaException() {

  data class TaskNotCreatedException(
    private val jobId: UUID,
    override val message: String = "Task not created for Job --- $jobId",
  ) : TaskException()

  data class UnknownTaskStatusException(
    private val taskStatusId: Int,
    override val message: String = "Unknown Task Status ID --- $taskStatusId",
  ) : TaskException()
}
