package karya.servers.server.domain.usecases.external

import karya.core.entities.responses.GetPlanResponse
import karya.core.exceptions.PlanException.PlanNotFoundException
import karya.core.exceptions.TaskException.TaskNotCreatedException
import karya.core.repos.PlansRepo
import karya.core.repos.TasksRepo
import java.util.*
import javax.inject.Inject

/**
 * Use case for retrieving a plan and its latest task.
 *
 * @property plansRepo The repository for accessing plans.
 * @property tasksRepo The repository for accessing tasks.
 */
class GetPlan
@Inject
constructor(
  private val plansRepo: PlansRepo,
  private val tasksRepo: TasksRepo,
) {

  /**
   * Retrieves the specified plan and its latest task.
   *
   * @param planId The ID of the plan to retrieve.
   * @return The response containing the plan and its latest task.
   * @throws PlanNotFoundException If the plan is not found.
   * @throws TaskNotCreatedException If the latest task for the plan is not found.
   */
  suspend fun invoke(planId: UUID): GetPlanResponse {
    val plan = plansRepo.get(planId) ?: throw PlanNotFoundException(planId)
    val latestTask = tasksRepo.getLatest(planId) ?: throw TaskNotCreatedException(planId)

    return GetPlanResponse(plan, latestTask)
  }
}
