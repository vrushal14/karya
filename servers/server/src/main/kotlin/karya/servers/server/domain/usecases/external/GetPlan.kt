package karya.servers.server.domain.usecases.external

import karya.core.entities.responses.GetPlanResponse
import karya.core.exceptions.PlanException.PlanNotFoundException
import karya.core.exceptions.TaskException.TaskNotCreatedException
import karya.core.repos.PlansRepo
import karya.core.repos.TasksRepo
import java.util.*
import javax.inject.Inject

class GetPlan
@Inject
constructor(
  private val plansRepo: PlansRepo,
  private val tasksRepo: TasksRepo,
) {
  suspend fun invoke(planId: UUID): GetPlanResponse {
    val plan = plansRepo.get(planId) ?: throw PlanNotFoundException(planId)
    val latestTask = tasksRepo.getLatest(planId) ?: throw TaskNotCreatedException(planId)

    return GetPlanResponse(plan, latestTask)
  }
}
