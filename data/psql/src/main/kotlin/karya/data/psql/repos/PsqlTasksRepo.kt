package karya.data.psql.repos

import karya.core.entities.Task
import karya.core.entities.enums.TaskStatus
import karya.core.repos.TasksRepo
import karya.core.repos.entities.GetTasksRequest
import karya.data.psql.tables.tasks.TasksQueries
import java.util.*
import javax.inject.Inject

class PsqlTasksRepo
@Inject
constructor(
  private val tasksQueries: TasksQueries,
) : TasksRepo {

  override suspend fun add(task: Task) {
    tasksQueries.add(task)
  }

  override suspend fun getLatest(jobId: UUID): Task? =
    tasksQueries.getLatest(jobId)

  override suspend fun get(request: GetTasksRequest): Task? =
    tasksQueries.get(request)

  override suspend fun update(task: Task) {
    tasksQueries.update(task)
  }

  override suspend fun updateStatus(id: UUID, status: TaskStatus) {
    tasksQueries.updateStatus(id, status)
  }

}