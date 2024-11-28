package karya.core.queues

import karya.core.queues.entities.ExecutorMessage
import karya.core.queues.entities.QueueType

interface QueueClient {
  suspend fun initialize()

  suspend fun push(message: ExecutorMessage, queueType: QueueType = QueueType.REGULAR)

  suspend fun consume(onMessage: suspend (ExecutorMessage) -> Unit)

  suspend fun shutdown(): Boolean
}
