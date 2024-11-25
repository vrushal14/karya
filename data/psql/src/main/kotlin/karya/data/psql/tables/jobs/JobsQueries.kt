package karya.data.psql.tables.jobs

import karya.core.entities.Job
import karya.core.entities.enums.JobStatus
import karya.data.psql.tables.jobs.mappers.JobStatusMapper
import karya.data.psql.tables.jobs.mappers.JobTypeMapper
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.*
import javax.inject.Inject

class JobsQueries
@Inject
constructor(
  private val db: Database,
  private val jobStatusMapper: JobStatusMapper,
  private val jobTypeMapper: JobTypeMapper,
) {
  fun add(job: Job) =
    transaction(db) {
      JobsTable.insert {
        it[id] = job.id
        it[userId] = job.userId
        it[periodTime] = job.periodTime
        it[type] = jobTypeMapper.toRecord(job.type)
        it[status] = jobStatusMapper.toRecord(job.status)
        it[maxFailureRetry] = job.maxFailureRetry
        it[action] = job.action
        it[createdAt] = Instant.ofEpochMilli(job.createdAt)
        it[updatedAt] = Instant.ofEpochMilli(job.updatedAt)
      }
    }

  fun get(id: UUID) =
    transaction(db) {
      JobsTable
        .selectAll()
        .where { JobsTable.id eq id }
        .firstOrNull()
    }?.let { fromRecord(it) }

  fun update(job: Job) =
    transaction(db) {
      JobsTable.update({ JobsTable.id eq job.id }) {
        it[status] = jobStatusMapper.toRecord(job.status)
        it[periodTime] = job.periodTime
        it[action] = job.action
        it[maxFailureRetry] = job.maxFailureRetry
        it[updatedAt] = Instant.ofEpochMilli(job.updatedAt)
      }
    }

  fun updateStatus(
    id: UUID,
    status: JobStatus,
  ) = transaction(db) {
    JobsTable.update({ JobsTable.id eq id }) {
      it[JobsTable.status] = jobStatusMapper.toRecord(status)
      it[updatedAt] = Instant.now()
    }
  }

  private fun fromRecord(resultRow: ResultRow) =
    Job(
      id = resultRow[JobsTable.id],
      userId = resultRow[JobsTable.userId],
      periodTime = resultRow[JobsTable.periodTime],
      type = jobTypeMapper.fromRecord(resultRow[JobsTable.type]),
      status = jobStatusMapper.fromRecord(resultRow[JobsTable.status]),
      maxFailureRetry = resultRow[JobsTable.maxFailureRetry],
      action = resultRow[JobsTable.action],
      createdAt = resultRow[JobsTable.createdAt].toEpochMilli(),
      updatedAt = resultRow[JobsTable.updatedAt].toEpochMilli(),
    )
}
