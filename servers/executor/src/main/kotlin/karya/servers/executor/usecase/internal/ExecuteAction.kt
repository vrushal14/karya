package karya.servers.executor.usecase.internal

import karya.core.actors.Connector
import karya.core.actors.Result
import karya.core.entities.action.Action
import karya.core.entities.enums.TaskStatus
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
  private val tasksRepo: TasksRepo
) {

  companion object : Logging

  suspend fun invoke(message: ExecutorMessage) {
    val connector = getConnector(message.action)
    when (val result = connector.invoke(message.action)) {
      is Result.Success -> handleSuccess(message)
      is Result.Failure -> handleFailure(result, message)
    }
  }

  private fun getConnector(action: Action) =
    config.connectors[action::class] as? Connector<Action>
      ?: throw ConnectorNotFoundException(action)

  private suspend fun handleSuccess(message: ExecutorMessage) =
    tasksRepo.updateStatus(message.taskId, TaskStatus.SUCCESS)
      .also { logger.info("[TASK EXECUTED SUCCESSFULLY] --- message : $message") }

  private suspend fun handleFailure(result: Result.Failure, message: ExecutorMessage) {
    val updatedMessage = message.copy(maxFailureRetry = message.maxFailureRetry - 1)
    if (message.maxFailureRetry <= 0) {
      queueClient.push(updatedMessage, QueueType.DEAD_LETTER)
      logger.error("[MESSAGE PUSHED TO DLQ] --- max retry exceeded | result : $result")

    } else {
      queueClient.push(updatedMessage)
      logger.warn("[ACTION FAILED | RETRYING] --- retrying action : $updatedMessage")
    }
    tasksRepo.updateStatus(message.taskId, TaskStatus.FAILURE)
  }

}
