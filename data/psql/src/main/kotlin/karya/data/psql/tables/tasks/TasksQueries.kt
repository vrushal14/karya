package karya.data.psql.tables.tasks

import karya.core.entities.Task
import karya.core.entities.enums.TaskStatus
import karya.core.repos.entities.GetTasksRequest
import karya.data.psql.tables.tasks.mappers.TaskStatusMapper
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class TasksQueries
@Inject
constructor(
  private val db : Database,
  private val taskStatusMapper: TaskStatusMapper
){

  fun add(task : Task) = transaction(db) {
    TasksTable.insert {
      it[id] = task.id
      it[jobId] = task.jobId
      it[partitionKey] = task.partitionKey
      it[status] = taskStatusMapper.toRecord(task.status)
      it[createdAt] = Instant.ofEpochMilli(task.createdAt)
      it[executedAt] = task.executedAt?.let { time -> Instant.ofEpochMilli(time) }
      it[nextExecutionAt] = task.nextExecutionAt?.let { time -> Instant.ofEpochMilli(time) }
    }
  }

  fun getLatest(jobId : UUID) = transaction(db) {
    TasksTable.selectAll()
      .where { TasksTable.jobId eq jobId }
      .orderBy(TasksTable.createdAt, SortOrder.DESC)
      .firstOrNull()
  }?.let { fromRecord(it) }

  fun get(request: GetTasksRequest) = transaction(db) {
    TasksTable.selectAll()
      .where { (TasksTable.partitionKey inList request.partitionKeys) and
          (TasksTable.nextExecutionAt greaterEq request.executionTime) and
          (TasksTable.nextExecutionAt lessEq request.executionTime.plus(request.buffer)) and
          (TasksTable.status eq taskStatusMapper.toRecord(request.status))
      }
      .orderBy(TasksTable.nextExecutionAt, SortOrder.DESC)
      .firstOrNull()
  }?.let { fromRecord(it) }

  fun update(task: Task) = transaction(db) {
    TasksTable.update({ TasksTable.id eq task.id }) {
      it[status] = taskStatusMapper.toRecord(task.status)
      it[executedAt] = task.executedAt?.let { time -> Instant.ofEpochMilli(time) }
    }
  }

  fun updateStatus(id : UUID, status: TaskStatus) = transaction(db) {
    TasksTable.update({ TasksTable.id eq id }) {
      it[TasksTable.status] = taskStatusMapper.toRecord(status)
    }
  }

  private fun fromRecord(resultRow: ResultRow) = Task(
    id = resultRow[TasksTable.id],
    jobId = resultRow[TasksTable.jobId],
    partitionKey = resultRow[TasksTable.partitionKey],
    status = taskStatusMapper.fromRecord(resultRow[TasksTable.status]),
    createdAt = resultRow[TasksTable.createdAt].toEpochMilli(),
    executedAt = resultRow[TasksTable.executedAt]?.toEpochMilli(),
    nextExecutionAt = resultRow[TasksTable.nextExecutionAt]?.toEpochMilli()
  )
}