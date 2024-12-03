package karya.data.psql.tables.errorlogs

import karya.data.psql.tables.jobs.JobsTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object ErrorLogsTable : Table("error_logs") {
  val jobId = uuid("job_id") references JobsTable.id
  val error = varchar("error", 255)
  val type = integer("type")
  val taskId = uuid("task_id").nullable()
  val timestamp = timestamp("timestamp")
}
