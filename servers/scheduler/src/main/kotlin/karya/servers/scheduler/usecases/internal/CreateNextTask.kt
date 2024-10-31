package karya.servers.scheduler.usecases.internal

import karya.core.connectors.RepoConnector
import karya.core.entities.Job
import karya.core.entities.Task
import karya.core.entities.enums.TaskStatus
import karya.core.repos.TasksRepo
import karya.core.usecases.createPartitionKey
import karya.core.usecases.getNextExecutionAt
import org.apache.logging.log4j.kotlin.Logging
import org.apache.logging.log4j.kotlin.logger
import java.time.Instant
import java.util.*
import javax.inject.Inject

class CreateNextTask
@Inject
constructor(
  private val tasksRepo: TasksRepo,
  private val repoConnector: RepoConnector
) {

  companion object : Logging

  suspend fun invoke(job: Job) = Task(
    id = UUID.randomUUID(),
    jobId = job.id,
    partitionKey = createPartitionKey(repoConnector.getPartitions()),
    status = TaskStatus.CREATED,
    createdAt = Instant.now().toEpochMilli(),
    executedAt = null,
    nextExecutionAt = getNextExecutionAt(Instant.now(), job.periodTime)
  )
    .also { tasksRepo.add(it) }
    .also { logger.info("[TASK CREATED] --- $it") }
}