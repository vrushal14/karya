package karya.data.psql.tables.jobs

import karya.core.entities.Job
import karya.core.entities.enums.JobStatus
import karya.data.psql.tables.jobs.mappers.JobStatusMapper
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.StatementType
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.*
import javax.inject.Inject

class JobsQueries
@Inject
constructor(
  private val db: Database,
  private val jobStatusMapper: JobStatusMapper,
) {

  fun add(job: Job) = transaction(db) {
    JobsTable.insert {
      it[id] = job.id
      it[userId] = job.userId
      it[description] = job.description
      it[periodTime] = job.periodTime
      it[type] = job.type
      it[status] = jobStatusMapper.toRecord(job.status)
      it[maxFailureRetry] = job.maxFailureRetry
      it[action] = job.action
      it[notificationSettings] = job.notificationSettings
      it[parentJobId] = job.parentJobId
      it[createdAt] = Instant.ofEpochMilli(job.createdAt)
      it[updatedAt] = Instant.ofEpochMilli(job.updatedAt)
    }
  }

  fun get(id: UUID) = transaction(db) {
    JobsTable
      .selectAll()
      .where { JobsTable.id eq id }
      .firstOrNull()
  }?.let(::fromRecord)

  fun update(job: Job) = transaction(db) {
    JobsTable.update({ JobsTable.id eq job.id }) {
      it[status] = jobStatusMapper.toRecord(job.status)
      it[periodTime] = job.periodTime
      it[action] = job.action
      it[maxFailureRetry] = job.maxFailureRetry
      it[updatedAt] = Instant.ofEpochMilli(job.updatedAt)
    }
  }

  fun updateStatus(id: UUID, status: JobStatus) = transaction(db) {
    JobsTable.update({ JobsTable.id eq id }) {
      it[JobsTable.status] = jobStatusMapper.toRecord(status)
      it[updatedAt] = Instant.now()
    }
  }

  fun getAllDescendantJobIds(jobId: UUID): List<UUID> = transaction(db) {
    val query = """
            WITH RECURSIVE ChildJobIds AS (
                SELECT id FROM jobs WHERE parent_job_id = '$jobId'
                UNION ALL
                SELECT j.id FROM jobs j INNER JOIN ChildJobIds cj ON j.parent_job_id = cj.id
            )
            SELECT id FROM ChildJobIds;
        """.trimIndent()

    exec(query, explicitStatementType = StatementType.SELECT) { resultSet ->
      val ids = mutableListOf<UUID>()
      while (resultSet.next()) {
        ids.add(UUID.fromString(resultSet.getString("id")))
      }
      ids
    } ?: emptyList()
  }

  private fun fromRecord(resultRow: ResultRow) = Job(
    id = resultRow[JobsTable.id],
    userId = resultRow[JobsTable.userId],
    description = resultRow[JobsTable.description],
    periodTime = resultRow[JobsTable.periodTime],
    type = resultRow[JobsTable.type],
    status = jobStatusMapper.fromRecord(resultRow[JobsTable.status]),
    maxFailureRetry = resultRow[JobsTable.maxFailureRetry],
    action = resultRow[JobsTable.action],
    notificationSettings = resultRow[JobsTable.notificationSettings],
    parentJobId = resultRow[JobsTable.parentJobId],
    createdAt = resultRow[JobsTable.createdAt].toEpochMilli(),
    updatedAt = resultRow[JobsTable.updatedAt].toEpochMilli(),
  )
}
