package karya.servers.server.domain.usecases.external

import karya.core.entities.responses.GetSummaryResponse
import karya.core.exceptions.PlanException
import karya.core.repos.ErrorLogsRepo
import karya.core.repos.PlansRepo
import karya.core.repos.TasksRepo
import java.util.*
import javax.inject.Inject

/**
 * Use case for retrieving a summary of a plan.
 *
 * @property plansRepo The repository for accessing plans.
 * @property tasksRepo The repository for accessing tasks.
 * @property errorLogsRepo The repository for accessing error logs.
 */
class GetSummary
@Inject
constructor(
  private val plansRepo: PlansRepo,
  private val tasksRepo: TasksRepo,
  private val errorLogsRepo: ErrorLogsRepo
) {

  /**
   * Retrieves a summary of the specified plan.
   *
   * @param planId The ID of the plan to retrieve the summary for.
   * @return The summary response containing the plan, tasks, and error logs.
   * @throws PlanException.PlanNotFoundException If the plan is not found.
   */
  suspend fun invoke(planId: UUID): GetSummaryResponse {
    val plan = plansRepo.get(planId) ?: throw PlanException.PlanNotFoundException(planId)
    val tasks = tasksRepo.get(planId).sortedBy { it.createdAt }
    val errorLogs = errorLogsRepo.get(plan.id)
    return GetSummaryResponse(plan, tasks, errorLogs)
  }
}
