package karya.servers.executor.usecase.internal

import karya.core.entities.ErrorLog
import karya.core.entities.ErrorLogType
import karya.core.entities.ExecutorResult
import karya.core.entities.enums.TaskStatus
import karya.core.queues.QueueClient
import karya.core.queues.entities.QueueMessage
import karya.core.queues.entities.QueueType
import karya.core.repos.ErrorLogsRepo
import karya.core.repos.TasksRepo
import org.apache.logging.log4j.kotlin.Logging
import java.time.Instant
import javax.inject.Inject

/**
 * Use case class responsible for processing tasks based on the executor result.
 *
 * @property tasksRepo The repository for managing tasks.
 * @property errorLogsRepo The repository for managing error logs.
 * @property queueClient The client for interacting with the queue.
 * @constructor Creates an instance of [ProcessTask] with the specified repositories and queue client.
 */
class ProcessTask
@Inject
constructor(
  private val tasksRepo: TasksRepo,
  private val errorLogsRepo: ErrorLogsRepo,
  private val queueClient: QueueClient
) {

  companion object : Logging

  /**
   * Processes the task based on the executor result.
   *
   * @param result The result of the executor's operation.
   * @param message The message containing task details.
   */
  suspend fun invoke(result: ExecutorResult, message: QueueMessage.ExecutorMessage) = when (result) {
    is ExecutorResult.Success -> handleSuccess(result, message)
    is ExecutorResult.Failure -> handleFailure(result, message)
  }

  /**
   * Handles the successful execution of a task.
   *
   * @param result The successful executor result.
   * @param message The message containing task details.
   */
  private suspend fun handleSuccess(result: ExecutorResult.Success, message: QueueMessage.ExecutorMessage) =
    tasksRepo.updateStatus(message.taskId, TaskStatus.SUCCESS, Instant.ofEpochMilli(result.timestamp))
      .also { logger.info("[TASK EXECUTED SUCCESSFULLY] --- [taskId : ${message.taskId} | result : $result]") }

  /**
   * Handles the failure of a task execution.
   *
   * @param result The failed executor result.
   * @param message The message containing task details.
   */
  private suspend fun handleFailure(result: ExecutorResult.Failure, message: QueueMessage.ExecutorMessage) {
    val updatedMessage = message.copy(maxFailureRetry = message.maxFailureRetry - 1)
    if (message.maxFailureRetry <= 0) {
      queueClient.push(updatedMessage, QueueType.DEAD_LETTER)
      logger.error("[MESSAGE PUSHED TO DLQ] --- max retry exceeded | result : $result")

    } else {
      queueClient.push(updatedMessage)
      logger.warn("[ACTION FAILED | RETRYING (${updatedMessage.maxFailureRetry})] --- result : ${result.reason}")
    }
    tasksRepo.updateStatus(message.taskId, TaskStatus.FAILURE, Instant.ofEpochMilli(result.timestamp))
    pushErrorLog(message, result)
  }

  /**
   * Pushes an error log to the repository.
   *
   * @param message The message containing task details.
   * @param result The failed executor result.
   */
  private suspend fun pushErrorLog(message: QueueMessage.ExecutorMessage, result: ExecutorResult.Failure) = ErrorLog(
    planId = message.planId,
    error = result.reason,
    type = ErrorLogType.ExecutorErrorLog(taskId = message.taskId),
    timestamp = result.timestamp
  ).let { errorLogsRepo.push(it) }
}
