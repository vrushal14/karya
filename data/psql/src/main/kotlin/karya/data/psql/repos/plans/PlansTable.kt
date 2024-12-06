package karya.data.psql.repos.plans

import karya.core.entities.Action
import karya.core.entities.Hook
import karya.core.entities.PlanType
import karya.data.psql.repos.users.UsersTable
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.json.json

object PlansTable : Table("plans") {
  val id = uuid("id")
  val userId = uuid("user_id") references UsersTable.id
  val description = varchar("description", 255)
  val periodTime = varchar("period_time", 255)
  val type = json<PlanType>("type", Json.Default)
  val status = integer("status")
  val maxFailureRetry = integer("max_failure_retry")
  val action = json<Action>("action", Json.Default)
  val hook = json<List<Hook>>("hook", Json.Default)
  val parentPlanId = uuid("parent_plan_id").nullable()

  val createdAt = timestamp("created_at")
  val updatedAt = timestamp("updated_at")

  override val primaryKey = PrimaryKey(id)
}
