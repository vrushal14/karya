package karya.core.repos

import karya.core.entities.Task
import karya.core.entities.enums.TaskStatus
import karya.core.repos.entities.GetTasksRequest
import java.util.UUID

interface TasksRepo {
	suspend fun add(task: Task)

	suspend fun getLatest(jobId: UUID): Task?

	suspend fun get(request: GetTasksRequest): Task?

	suspend fun update(task: Task)

	suspend fun updateStatus(
		id: UUID,
		status: TaskStatus,
	)
}
