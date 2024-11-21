package karya.data.rabbitmq

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import karya.core.queues.entities.ExecutorMessage
import karya.core.queues.QueueClient
import org.apache.logging.log4j.kotlin.Logging

class RabbitMqQueueClientClient(
  private val channel: Channel,
  private val connection : Connection
) : QueueClient {

  companion object : Logging

  override suspend fun push(message: ExecutorMessage) {
    TODO("Not yet implemented")
  }

  override suspend fun shutdown(): Boolean = try {
    channel.close()
    connection.close()
    true
  } catch (e: Exception) {
    logger.error("Error shutting down RabbitMQ client --- $e")
    false
  }
}