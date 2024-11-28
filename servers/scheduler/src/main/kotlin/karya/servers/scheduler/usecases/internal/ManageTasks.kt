package karya.servers.scheduler.usecases.internal

import karya.core.entities.Job
import karya.core.entities.Task
import karya.core.entities.enums.JobStatus
import karya.core.entities.enums.JobType
import karya.core.entities.enums.TaskStatus
import karya.core.queues.QueueClient
import karya.core.queues.entities.ExecutorMessage
import karya.core.repos.RepoConnector
import karya.core.repos.TasksRepo
import karya.core.usecases.createPartitionKey
import karya.core.usecases.getNextExecutionAt
import karya.servers.scheduler.usecases.utils.getInstanceName
import org.apache.logging.log4j.kotlin.Logging
import org.apache.logging.log4j.kotlin.logger
import java.time.Instant
import java.util.*
import javax.inject.Inject

class ManageTasks
@Inject
constructor(
  private val tasksRepo: TasksRepo,
  private val repoConnector: RepoConnector,
  private val queueClient: QueueClient,
) {
  companion object : Logging

  suspend fun invoke(job: Job, task: Task) {
    pushCurrentTaskToQueue(job, task)
    if (shouldCreateNextTask(job)) createNextTask(job)
  }

  private suspend fun pushCurrentTaskToQueue(job: Job, task: Task) = ExecutorMessage(
    jobId = job.id,
    taskId = task.id,
    action = job.action,
    maxFailureRetry = job.maxFailureRetry,
  ).also { queueClient.push(it) }

  private suspend fun shouldCreateNextTask(job: Job): Boolean {
    val isJobNonTerminal = (job.status == JobStatus.CREATED).or(job.status == JobStatus.RUNNING)
    val isJobRecurring = (job.type == JobType.RECURRING)
    return (isJobRecurring && isJobNonTerminal)
      .also { logger.info("[${getInstanceName()}] : shouldCreateNextJob : $it") }
  }

  private suspend fun createNextTask(job: Job) = Task(
    id = UUID.randomUUID(),
    jobId = job.id,
    partitionKey = createPartitionKey(repoConnector.getPartitions()),
    status = TaskStatus.CREATED,
    createdAt = Instant.now().toEpochMilli(),
    executedAt = null,
    nextExecutionAt = getNextExecutionAt(Instant.now(), job.periodTime),
  ).also { tasksRepo.add(it) }
    .also { logger.info("[${getInstanceName()}] : [NEXT TASK CREATED] --- $it") }
}


//11:13 -> job submitted
//11:28 -> Scheduler Picks up
//11:29 -> Chained job+task created
//11:29 ->
