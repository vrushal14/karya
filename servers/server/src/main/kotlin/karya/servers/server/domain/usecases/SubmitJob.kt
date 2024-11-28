package karya.servers.server.domain.usecases

import karya.core.entities.Job
import karya.core.entities.Task
import karya.core.entities.User
import karya.core.entities.enums.JobStatus
import karya.core.entities.enums.TaskStatus
import karya.core.entities.requests.SubmitJobRequest
import karya.core.exceptions.UserException
import karya.core.repos.JobsRepo
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

class SubmitJob
@Inject
constructor(
  private val usersRepo: UsersRepo,
  private val jobsRepo: JobsRepo,
  private val tasksRepo: TasksRepo,
  private val repoConnector: RepoConnector,
) {

  companion object : Logging

  suspend fun invoke(request: SubmitJobRequest): Job = usersRepo
    .get(request.userId)
    ?.let { createJob(request, it) }
    ?.also { createTask(it) }
    ?: throw UserException.UserNotFoundException(request.userId)

  private suspend fun createJob(request: SubmitJobRequest, user: User) = Job(
    id = UUID.randomUUID(),
    userId = user.id,
    description = request.description,
    periodTime = request.periodTime,
    type = request.jobType,
    status = JobStatus.CREATED,
    maxFailureRetry = request.maxFailureRetry,
    action = request.action,
    createdAt = Instant.now().toEpochMilli(),
    updatedAt = Instant.now().toEpochMilli(),
  ).also { jobsRepo.add(it) }
    .also { logger.info("[JOB CREATED] --- $it") }

  private suspend fun createTask(job: Job) = Task(
    id = UUID.randomUUID(),
    jobId = job.id,
    partitionKey = createPartitionKey(repoConnector.getPartitions()),
    status = TaskStatus.CREATED,
    createdAt = Instant.now().toEpochMilli(),
    executedAt = null,
    nextExecutionAt = getNextExecutionAt(Instant.ofEpochMilli(job.createdAt), job.periodTime),
  ).also { tasksRepo.add(it) }
    .also { logger.info("[TASK CREATED] --- $it") }
}
