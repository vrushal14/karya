package karya.data.psql.repos.tasks

import karya.data.psql.repos.plans.PlansTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object TasksTable : Table("tasks") {
  val id = uuid("id")
  val planId = uuid("plan_id") references PlansTable.id
  val partitionKey = integer("partition_key")
  val status = integer("status")
  val createdAt = timestamp("created_at")
  val executedAt = timestamp("executed_at").nullable()
  val nextExecutionAt = timestamp("next_execution_at").nullable()

  override val primaryKey = PrimaryKey(id, partitionKey)
}
