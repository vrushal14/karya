package karya.servers.scheduler.usecases.internal

import karya.core.entities.Job
import karya.core.entities.Task
import karya.core.entities.enums.TaskStatus
import karya.core.entities.messages.ExecutorMessage
import karya.core.repos.RepoConnector
import karya.core.repos.TasksRepo
import karya.core.usecases.createPartitionKey
import karya.core.usecases.getNextExecutionAt
import org.apache.logging.log4j.kotlin.Logging
import org.apache.logging.log4j.kotlin.logger
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class ManageTasks
@Inject
constructor(
  private val tasksRepo: TasksRepo,
  private val repoConnector: RepoConnector
){
  companion object : Logging

  suspend fun invoke(job: Job, task: Task) {
    createNextTask(job)
    pushCurrentTaskToQueue(job, task)
  }

  private suspend fun createNextTask(job: Job) = Task(
    id = UUID.randomUUID(),
    jobId = job.id,
    partitionKey = createPartitionKey(repoConnector.getPartitions()),
    status = TaskStatus.CREATED,
    createdAt = Instant.now().toEpochMilli(),
    executedAt = null,
    nextExecutionAt = getNextExecutionAt(Instant.now(), job.periodTime)
  )
    .also { tasksRepo.add(it) }
    .also { logger.info("[NEXT TASK CREATED] --- $it") }

  private suspend fun pushCurrentTaskToQueue(job: Job, task: Task) = ExecutorMessage(
    jobId = job.id,
    taskId = task.id,
    action = job.action,
    maxFailureRetry = job.maxFailureRetry
  ).also { logger.info { "[PUSHING TO EXECUTOR QUEUE] --- $it" } }
}