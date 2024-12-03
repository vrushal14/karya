package karya.core.repos

import karya.core.entities.Plan
import karya.core.entities.enums.PlanStatus
import java.util.*

interface PlansRepo {
  suspend fun add(plan: Plan)

  suspend fun get(id: UUID): Plan?

  suspend fun update(plan: Plan)

  suspend fun updateStatus(id: UUID, status: PlanStatus)

  suspend fun getChildPlanIds(id: UUID): List<UUID>
}
