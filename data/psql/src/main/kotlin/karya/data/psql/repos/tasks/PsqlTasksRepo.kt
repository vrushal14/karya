package karya.data.psql.repos.tasks

import karya.core.entities.Task
import karya.core.entities.enums.TaskStatus
import karya.core.repos.TasksRepo
import karya.core.repos.entities.GetTasksRequest
import karya.data.psql.repos.tasks.mappers.TaskStatusMapper
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.*
import javax.inject.Inject

class PsqlTasksRepo
@Inject
constructor(
  private val db: Database,
  private val taskStatusMapper: TaskStatusMapper,
) : TasksRepo {

  companion object {
    private const val RANGE_QUERY_LIMIT = 1
  }

  override suspend fun add(task: Task) {
    transaction(db) {
      TasksTable.insert {
        it[id] = task.id
        it[planId] = task.planId
        it[partitionKey] = task.partitionKey
        it[status] = taskStatusMapper.toRecord(task.status)
        it[createdAt] = Instant.ofEpochMilli(task.createdAt)
        it[executedAt] = task.executedAt?.let { time -> Instant.ofEpochMilli(time) }
        it[nextExecutionAt] = task.nextExecutionAt?.let { time -> Instant.ofEpochMilli(time) }
      }
    }
  }

  override suspend fun getLatest(planId: UUID): Task? = transaction(db) {
    TasksTable
      .selectAll()
      .where { TasksTable.planId eq planId }
      .orderBy(TasksTable.createdAt, SortOrder.DESC)
      .firstOrNull()
  }?.let(::fromRecord)

  override suspend fun get(request: GetTasksRequest): Task? = transaction(db) {
    TasksTable
      .selectAll()
      .where {
        (TasksTable.partitionKey inList request.partitionKeys) and
            (TasksTable.status eq taskStatusMapper.toRecord(request.status)) and
            (TasksTable.nextExecutionAt lessEq request.executionTime) and
            (TasksTable.nextExecutionAt greaterEq request.executionTime.minus(request.buffer)) and
            (TasksTable.nextExecutionAt.isNotNull())
      }
      .orderBy(TasksTable.nextExecutionAt)
      .limit(RANGE_QUERY_LIMIT)
      .firstOrNull()
      ?.let(::fromRecord)
  }

  override suspend fun get(planId: UUID): List<Task> = transaction(db) {
    TasksTable
      .selectAll()
      .where { TasksTable.planId eq planId }
      .map(::fromRecord)
  }

  override suspend fun update(task: Task) {
    transaction(db) {
      TasksTable.update({ TasksTable.id eq task.id }) {
        it[status] = taskStatusMapper.toRecord(task.status)
        it[executedAt] = task.executedAt?.let { time -> Instant.ofEpochMilli(time) }
      }
    }
  }

  override suspend fun updateStatus(id: UUID, status: TaskStatus) {
    transaction(db) {
      TasksTable.update({ TasksTable.id eq id }) {
        it[TasksTable.status] = taskStatusMapper.toRecord(status)
      }
    }
  }

  override suspend fun updateStatus(id: UUID, status: TaskStatus, executedAt: Instant) {
    transaction(db) {
      TasksTable.update({ TasksTable.id eq id }) {
        it[TasksTable.status] = taskStatusMapper.toRecord(status)
        it[TasksTable.executedAt] = executedAt
      }
    }
  }

  private fun fromRecord(resultRow: ResultRow) = Task(
    id = resultRow[TasksTable.id],
    planId = resultRow[TasksTable.planId],
    partitionKey = resultRow[TasksTable.partitionKey],
    status = taskStatusMapper.fromRecord(resultRow[TasksTable.status]),
    createdAt = resultRow[TasksTable.createdAt].toEpochMilli(),
    executedAt = resultRow[TasksTable.executedAt]?.toEpochMilli(),
    nextExecutionAt = resultRow[TasksTable.nextExecutionAt]?.toEpochMilli(),
  )
}
