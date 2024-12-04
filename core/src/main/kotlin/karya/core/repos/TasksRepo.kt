package karya.core.repos

import karya.core.entities.Task
import karya.core.entities.enums.TaskStatus
import karya.core.repos.entities.GetTasksRequest
import java.time.Instant
import java.util.*

/**
 * Interface representing a repository for managing tasks.
 */
interface TasksRepo {

  /**
   * Adds a new task to the repository.
   *
   * @param task The task to be added.
   */
  suspend fun add(task: Task)

  /**
   * Retrieves the latest task for a specific plan.
   *
   * @param planId The unique identifier of the plan.
   * @return The latest task associated with the specified plan, or `null` if not found.
   */
  suspend fun getLatest(planId: UUID): Task?

  /**
   * Retrieves a task based on the specified request parameters.
   *
   * @param request The request parameters to filter tasks.
   * @return The task matching the request parameters, or `null` if not found.
   */
  suspend fun get(request: GetTasksRequest): Task?

  /**
   * Retrieves all tasks for a specific plan.
   *
   * @param planId The unique identifier of the plan.
   * @return A list of tasks associated with the specified plan.
   */
  suspend fun get(planId: UUID): List<Task>

  /**
   * Updates an existing task in the repository.
   *
   * @param task The task with updated information.
   */
  suspend fun update(task: Task)

  /**
   * Updates the status of a task.
   *
   * @param id The unique identifier of the task.
   * @param status The new status to be set for the task.
   */
  suspend fun updateStatus(id: UUID, status: TaskStatus)

  /**
   * Updates the status and execution time of a task.
   *
   * @param id The unique identifier of the task.
   * @param status The new status to be set for the task.
   * @param executedAt The execution time to be set for the task.
   */
  suspend fun updateStatus(id: UUID, status: TaskStatus, executedAt: Instant)
}
