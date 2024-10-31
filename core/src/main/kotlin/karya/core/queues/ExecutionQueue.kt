package karya.core.queues

import karya.core.entities.messages.ExecutorMessage

interface ExecutionQueue {
  suspend fun push(message: ExecutorMessage)
}