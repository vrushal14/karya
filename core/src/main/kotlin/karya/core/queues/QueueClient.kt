package karya.core.queues

import karya.core.queues.entities.ExecutorMessage

interface QueueClient {
  suspend fun push(message: ExecutorMessage)
  suspend fun shutdown() : Boolean
}