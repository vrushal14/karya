package karya.data.psql.repos.errorlogs

import karya.data.psql.repos.plans.PlansTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object ErrorLogsTable : Table("error_logs") {
  val planId = uuid("plan_id") references PlansTable.id
  val error = varchar("error", 255)
  val type = integer("type")
  val taskId = uuid("task_id").nullable()
  val timestamp = timestamp("timestamp")
}
