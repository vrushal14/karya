package karya.servers.server.domain.usecases.external

import karya.core.entities.responses.GetSummaryResponse
import karya.core.exceptions.PlanException
import karya.core.repos.ErrorLogsRepo
import karya.core.repos.PlansRepo
import karya.core.repos.TasksRepo
import java.util.*
import javax.inject.Inject

class GetSummary
@Inject
constructor(
  private val plansRepo: PlansRepo,
  private val tasksRepo: TasksRepo,
  private val errorLogsRepo: ErrorLogsRepo
) {

  suspend fun invoke(planId: UUID): GetSummaryResponse {
    val plan = plansRepo.get(planId) ?: throw PlanException.PlanNotFoundException(planId)
    val tasks = tasksRepo.get(planId).sortedBy { it.createdAt }
    val errorLogs = errorLogsRepo.get(plan.id)
    return GetSummaryResponse(plan, tasks, errorLogs)
  }
}
