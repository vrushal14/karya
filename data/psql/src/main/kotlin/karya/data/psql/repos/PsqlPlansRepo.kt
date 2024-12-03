package karya.data.psql.repos

import karya.core.entities.Plan
import karya.core.entities.enums.PlanStatus
import karya.core.repos.PlansRepo
import karya.data.psql.tables.plans.PlansQueries
import java.util.*
import javax.inject.Inject

class PsqlPlansRepo
@Inject
constructor(
  private val plansQueries: PlansQueries,
) : PlansRepo {
  override suspend fun add(plan: Plan) {
    plansQueries.add(plan)
  }

  override suspend fun get(id: UUID): Plan? = plansQueries.get(id)

  override suspend fun update(plan: Plan) {
    plansQueries.update(plan)
  }

  override suspend fun updateStatus(id: UUID, status: PlanStatus) {
    plansQueries.updateStatus(id, status)
  }

  override suspend fun getChildPlanIds(id: UUID): List<UUID> =
    plansQueries.getAllDescendantPlanIds(id)
}
