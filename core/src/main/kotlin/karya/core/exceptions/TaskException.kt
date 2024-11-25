package karya.core.exceptions

import java.util.*

sealed class TaskException(
    override val message: String,
) : KaryaException(message) {
    data class TaskNotCreatedException(
        val jobId: UUID,
        override val message: String = "Task not created for Job --- $jobId",
    ) : TaskException(message)

    data class UnknownTaskStatusException(
        val taskStatusId: Int,
        override val message: String = "Unknown Task Status ID --- $taskStatusId",
    ) : TaskException(message)
}
