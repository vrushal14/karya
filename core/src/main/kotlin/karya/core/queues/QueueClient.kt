package karya.core.queues

import karya.core.queues.entities.QueueMessage
import karya.core.queues.entities.QueueType

/**
 * Interface representing a client for managing queue operations.
 */
interface QueueClient {

  /**
   * Initializes the queue client.
   */
  suspend fun initialize()

  /**
   * Pushes a message to the specified queue.
   *
   * @param message The message to be pushed to the queue.
   * @param queueType The type of the queue to which the message will be pushed. Defaults to [QueueType.EXECUTOR].
   */
  suspend fun push(message: QueueMessage, queueType: QueueType = QueueType.EXECUTOR)

  /**
   * Consumes messages from the queue and processes them using the provided handler.
   *
   * @param onMessage The handler function to process each consumed message.
   */
  suspend fun consume(onMessage: suspend (QueueMessage) -> Unit)

  /**
   * Shuts down the queue client.
   *
   * @return `true` if the shutdown was successful, `false` otherwise.
   */
  suspend fun shutdown(): Boolean
}
