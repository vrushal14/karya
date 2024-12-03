package karya.core.exceptions

import java.util.*

sealed class TaskException : KaryaException() {

  data class TaskNotCreatedException(
    private val planId: UUID,
    override val message: String = "Task not created for Plan --- $planId",
  ) : TaskException()

  data class UnknownTaskStatusException(
    private val taskStatusId: Int,
    override val message: String = "Unknown Task Status ID --- $taskStatusId",
  ) : TaskException()
}
