package karya.data.rabbitmq

import karya.core.entities.messages.ExecutorMessage
import karya.core.queues.ExecutionQueue
import javax.inject.Inject

class RabbitMqQueueClient
@Inject
constructor(

) : ExecutionQueue {

  override suspend fun push(message: ExecutorMessage) {
    TODO("Not yet implemented")
  }
}