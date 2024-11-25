package karya.core.queues

import karya.core.queues.entities.ExecutorMessage

interface QueueClient {
  suspend fun push(message: ExecutorMessage)

  suspend fun consume(onMessage: suspend (ExecutorMessage) -> Unit)

  suspend fun shutdown(): Boolean
}
