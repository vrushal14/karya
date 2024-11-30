package karya.servers.executor.usecase.internal

import karya.core.actors.Connector
import karya.core.actors.Result
import karya.core.entities.action.Action
import karya.core.entities.enums.TaskStatus.FAILURE
import karya.core.entities.enums.TaskStatus.SUCCESS
import karya.core.exceptions.KaryaException
import karya.core.queues.QueueClient
import karya.core.queues.entities.ExecutorMessage
import karya.core.queues.entities.QueueType
import karya.core.repos.TasksRepo
import karya.servers.executor.configs.ExecutorConfig
import karya.servers.executor.exceptions.ExecutorException.ConnectorNotFoundException
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class ExecuteAction
@Inject
constructor(
  private val config: ExecutorConfig,
  private val queueClient: QueueClient,
  private val tasksRepo: TasksRepo,
  private val maybeUpdateJob: MaybeUpdateJob
) {

  companion object : Logging

  suspend fun invoke(message: ExecutorMessage) = try {
    val connector = getConnector(message.action)
    when (val result = connector.invoke(message.jobId, message.action)) {
      is Result.Success -> handleSuccess(result, message)
      is Result.Failure -> handleFailure(result, message)
    }
    maybeUpdateJob.invoke(message.jobId)

  } catch (e: KaryaException) {
    logger.error(e) { "[PUSHING MESSAGE TO DLQ] Exception while executing action --- ${e.message}" }
    queueClient.push(message, QueueType.DEAD_LETTER)
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
}
