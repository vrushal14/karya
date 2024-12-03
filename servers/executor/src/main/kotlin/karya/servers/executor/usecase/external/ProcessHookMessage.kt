package karya.servers.executor.usecase.external

import karya.core.entities.ErrorLog
import karya.core.entities.ErrorLogType
import karya.core.entities.ExecutorResult
import karya.core.queues.QueueClient
import karya.core.queues.entities.QueueMessage
import karya.core.queues.entities.QueueType
import karya.core.repos.ErrorLogsRepo
import karya.servers.executor.usecase.internal.GetConnector
import org.apache.logging.log4j.kotlin.Logging
import org.apache.logging.log4j.kotlin.logger
import javax.inject.Inject

class ProcessHookMessage
@Inject
constructor(
  private val getConnector: GetConnector,
  private val queueClient: QueueClient,
  private val errorLogsRepo: ErrorLogsRepo
) {

  companion object : Logging

  suspend fun invoke(message: QueueMessage.HookMessage) {
    val connector = getConnector.invoke(message.hook.action)
    val result = connector.invoke(message.jobId, message.hook.action)
    when (result) {
      is ExecutorResult.Success -> logger.info("[HOOK ASYNC] --- hook processed successfully")
      is ExecutorResult.Failure -> if (shouldRetryFailedHook(message, result)) retryHook(message)
    }
  }

  private suspend fun shouldRetryFailedHook(message: QueueMessage.HookMessage, result: ExecutorResult.Failure) =
    (message.hook.maxRetry > 0)
      .also { pushErrorLogs(message, result) }
      .also { logger.info("[RETRY HOOK ASYNC | maxRetry : ${message.hook.maxRetry}] --- result : $result") }

  private suspend fun retryHook(message: QueueMessage.HookMessage) {
    val updatedHook = message.hook.copy(maxRetry = message.hook.maxRetry - 1)
    val updatedMessage = message.copy(hook = updatedHook)
    queueClient.push(updatedMessage, QueueType.HOOK)
  }

  private suspend fun pushErrorLogs(message: QueueMessage.HookMessage, result: ExecutorResult.Failure) = ErrorLog(
    jobId = message.jobId,
    error = result.reason,
    type = ErrorLogType.HookErrorLog,
    timestamp = result.timestamp
  ).let { errorLogsRepo.push(it) }
}
