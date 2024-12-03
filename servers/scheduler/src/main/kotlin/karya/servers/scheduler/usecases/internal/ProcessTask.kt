package karya.servers.scheduler.usecases.internal

import karya.core.entities.Plan
import karya.core.entities.Task
import karya.core.entities.enums.PlanStatus
import karya.core.entities.enums.TaskStatus
import karya.core.exceptions.PlanException.PlanNotFoundException
import karya.core.locks.LocksClient
import karya.core.locks.entities.LockResult
import karya.core.repos.PlansRepo
import karya.core.repos.TasksRepo
import karya.servers.scheduler.usecases.utils.getInstanceName
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class ProcessTask
@Inject
constructor(
  private val tasksRepo: TasksRepo,
  private val plansRepo: PlansRepo,
  private val locksClient: LocksClient,
  private val manageTasks: ManageTasks,
) {
  companion object : Logging

  suspend fun invoke(task: Task): Boolean {
    val result = locksClient.withLock(task.id) { processTaskInternal(task) }
    return when (result) {
      is LockResult.Success -> true
      is LockResult.Failure -> false
    }
  }

  private suspend fun processTaskInternal(task: Task) {
    logger.info("[${getInstanceName()}] : [PROCESSING TASK] --- TaskId : ${task.id}")
    val plan = plansRepo.get(task.planId) ?: throw PlanNotFoundException(task.planId)
    updateTaskStatus(plan, task)

    val updatedPlan = transitionPlanStatus(plan)
    manageTasks.invoke(updatedPlan, task)
  }

  private suspend fun updateTaskStatus(plan: Plan, task: Task) = when (plan.status) {
    PlanStatus.CREATED, PlanStatus.RUNNING -> TaskStatus.PROCESSING
    PlanStatus.COMPLETED -> TaskStatus.SUCCESS
    PlanStatus.CANCELLED -> TaskStatus.CANCELLED
  }.also {
    if (task.status != it) {
      tasksRepo.updateStatus(task.id, it)
      logger.info("[${getInstanceName()}] : Transitioning task status from : ${task.status} -> $it")
    }
  }

  private suspend fun transitionPlanStatus(plan: Plan) = when (plan.status) {
    PlanStatus.CREATED -> plan
      .copy(status = PlanStatus.RUNNING)
      .also { plansRepo.updateStatus(plan.id, PlanStatus.RUNNING) }
      .also { logger.info("[${getInstanceName()}] : Plan Status updated : ${plan.status} -> ${it.status}") }

    else -> plan
  }
}
