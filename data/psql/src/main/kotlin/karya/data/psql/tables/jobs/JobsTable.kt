package karya.data.psql.tables.jobs

import karya.data.psql.tables.users.UsersTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object JobsTable : Table("jobs") {
  val id = uuid("id")
  val userId = uuid("user_id") references UsersTable.id
  val periodTime = varchar("period_time", 255)
  val type = integer("type")
  val status = integer("status")
  val maxFailureRetry = integer("max_failure_retry")
  val executorEndpoint = varchar("executor_endpoint", 255)

  val createdAt = timestamp("created_at")
  val updatedAt = timestamp("updated_at")

  override val primaryKey = PrimaryKey(id)
}