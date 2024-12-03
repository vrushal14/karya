package karya.servers.server.domain.usecases.external

import karya.core.entities.Plan
import karya.core.entities.Task
import karya.core.entities.User
import karya.core.entities.enums.PlanStatus
import karya.core.entities.enums.TaskStatus
import karya.core.entities.requests.SubmitPlanRequest
import karya.core.exceptions.UserException
import karya.core.repos.PlansRepo
import karya.core.repos.RepoConnector
import karya.core.repos.TasksRepo
import karya.core.repos.UsersRepo
import karya.core.usecases.createPartitionKey
import karya.core.usecases.getNextExecutionAt
import org.apache.logging.log4j.kotlin.Logging
import org.apache.logging.log4j.kotlin.logger
import java.time.Instant
import java.util.*
import javax.inject.Inject

class SubmitPlan
@Inject
constructor(
  private val usersRepo: UsersRepo,
  private val plansRepo: PlansRepo,
  private val tasksRepo: TasksRepo,
  private val repoConnector: RepoConnector,
) {

  companion object : Logging

  suspend fun invoke(request: SubmitPlanRequest, parentPlanId: UUID?): Plan = usersRepo
    .get(request.userId)
    ?.let { createPlan(request, it, parentPlanId) }
    ?.also { createTask(it) }
    ?: throw UserException.UserNotFoundException(request.userId)

  private suspend fun createPlan(request: SubmitPlanRequest, user: User, parentPlanId: UUID?) = Plan(
    id = UUID.randomUUID(),
    userId = user.id,
    description = request.description,
    periodTime = request.periodTime,
    type = request.planType,
    status = PlanStatus.CREATED,
    maxFailureRetry = request.maxFailureRetry,
    action = request.action,
    parentPlanId = parentPlanId,
    hook = request.hooks,
    createdAt = Instant.now().toEpochMilli(),
    updatedAt = Instant.now().toEpochMilli(),
  ).also { plansRepo.add(it) }
    .also { logger.info("[PLAN CREATED] --- $it") }

  private suspend fun createTask(plan: Plan) = Task(
    id = UUID.randomUUID(),
    planId = plan.id,
    partitionKey = createPartitionKey(repoConnector.getPartitions()),
    status = TaskStatus.CREATED,
    createdAt = Instant.now().toEpochMilli(),
    executedAt = null,
    nextExecutionAt = getNextExecutionAt(Instant.ofEpochMilli(plan.createdAt), plan.periodTime),
  ).also { tasksRepo.add(it) }
    .also { logger.info("[TASK CREATED] --- $it") }
}
