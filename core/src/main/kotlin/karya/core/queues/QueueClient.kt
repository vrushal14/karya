package karya.core.queues

import karya.core.queues.entities.QueueMessage
import karya.core.queues.entities.QueueType

interface QueueClient {
  suspend fun initialize()

  suspend fun push(message: QueueMessage, queueType: QueueType = QueueType.EXECUTOR)

  suspend fun consume(onMessage: suspend (QueueMessage) -> Unit)

  suspend fun shutdown(): Boolean
}
