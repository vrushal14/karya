package karya.data.psql.tables.jobs

import karya.core.entities.JobType
import karya.core.entities.action.Action
import karya.data.psql.tables.users.UsersTable
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.json.json

object JobsTable : Table("jobs") {
  val id = uuid("id")
  val userId = uuid("user_id") references UsersTable.id
  val description = varchar("description", 255)
  val periodTime = varchar("period_time", 255)
  val type = json<JobType>("type", Json)
  val status = integer("status")
  val maxFailureRetry = integer("max_failure_retry")
  val action = json<Action>("action", Json)
  val parentJobId = uuid("parent_job_id").nullable()

  val createdAt = timestamp("created_at")
  val updatedAt = timestamp("updated_at")

  override val primaryKey = PrimaryKey(id)
}
