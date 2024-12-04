package karya.servers.executor.usecase.external

import karya.core.queues.entities.QueueMessage
import karya.core.queues.entities.QueueMessage.ExecutorMessage
import karya.core.queues.entities.QueueMessage.HookMessage
import javax.inject.Inject

/**
 * Use case class responsible for processing different types of messages.
 *
 * @property processExecutorMessage The use case for processing executor messages.
 * @property processHookMessage The use case for processing hook messages.
 * @constructor Creates an instance of [ProcessMessage] with the specified dependencies.
 */
class ProcessMessage
@Inject
constructor(
  private val processExecutorMessage: ProcessExecutorMessage,
  private val processHookMessage: ProcessHookMessage
) {

  /**
   * Processes the given message based on its type.
   *
   * @param message The message to be processed.
   */
  suspend fun invoke(message: QueueMessage) = when (message) {
    is ExecutorMessage -> processExecutorMessage.invoke(message)
    is HookMessage -> processHookMessage.invoke(message)
  }
}
