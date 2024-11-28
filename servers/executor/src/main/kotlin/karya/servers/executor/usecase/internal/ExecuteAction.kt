package karya.servers.executor.usecase.internal

import karya.core.actors.Connector
import karya.core.actors.Result
import karya.core.entities.action.Action
import karya.core.entities.enums.JobStatus
import karya.core.entities.enums.JobType
import karya.core.entities.enums.TaskStatus.FAILURE
import karya.core.entities.enums.TaskStatus.SUCCESS
import karya.core.exceptions.JobException.JobNotFoundException
import karya.core.queues.QueueClient
import karya.core.queues.entities.ExecutorMessage
import karya.core.queues.entities.QueueType
import karya.core.repos.JobsRepo
import karya.core.repos.TasksRepo
import karya.servers.executor.configs.ExecutorConfig
import karya.servers.executor.exceptions.ExecutorException.ConnectorNotFoundException
import org.apache.logging.log4j.kotlin.Logging
import java.util.*
import javax.inject.Inject

class ExecuteAction
@Inject
constructor(
  private val config: ExecutorConfig,
  private val queueClient: QueueClient,
  private val tasksRepo: TasksRepo,
  private val jobsRepo: JobsRepo
) {

  companion object : Logging

  suspend fun invoke(message: ExecutorMessage) {
    val connector = getConnector(message.action)
    when (val result = connector.invoke(message.action)) {
      is Result.Success -> handleSuccess(result, message)
      is Result.Failure -> handleFailure(result, message)
    }
    maybeUpdateJob(message.jobId)
  }

  private fun getConnector(action: Action) =
    config.connectors[action::class] as? Connector<Action>
      ?: throw ConnectorNotFoundException(action)

  private suspend fun handleSuccess(result: Result.Success, message: ExecutorMessage) =
    tasksRepo.updateStatus(message.taskId, SUCCESS, result.timestamp)
      .also { logger.info("[TASK EXECUTED SUCCESSFULLY] --- [taskId : ${message.taskId} | result : $result]") }

  private suspend fun handleFailure(result: Result.Failure, message: ExecutorMessage) {
    val updatedMessage = message.copy(maxFailureRetry = message.maxFailureRetry - 1)
    if (message.maxFailureRetry <= 0) {
      queueClient.push(updatedMessage, QueueType.DEAD_LETTER)
      logger.error("[MESSAGE PUSHED TO DLQ] --- max retry exceeded | result : $result")

    } else {
      queueClient.push(updatedMessage)
      logger.warn("[ACTION FAILED | RETRYING (${updatedMessage.maxFailureRetry})] --- result : ${result.reason}")
    }
    tasksRepo.updateStatus(message.taskId, FAILURE, result.timestamp)
  }

  private suspend fun maybeUpdateJob(jobId: UUID) {
    val job = jobsRepo.get(jobId) ?: throw JobNotFoundException(jobId)
    when (job.type) {
      JobType.RECURRING -> return
      JobType.ONE_TIME -> jobsRepo.updateStatus(jobId, JobStatus.COMPLETED)
    }
    logger.info("[JOB COMPLETED] --- jobId : $jobId")
  }
}
