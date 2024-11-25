package karya.data.rabbitmq

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import karya.core.queues.entities.ExecutorMessage
import karya.data.rabbitmq.RabbitMqQueueClient.Companion.json
import kotlinx.coroutines.runBlocking
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class RabbitMqConsumer
@Inject
constructor(
  private val channel: Channel,
) : DefaultConsumer(channel) {
  companion object : Logging

  var onMessage: (suspend (ExecutorMessage) -> Unit)? = null

  override fun handleDelivery(
    consumerTag: String?,
    envelope: Envelope?,
    properties: AMQP.BasicProperties?,
    body: ByteArray?,
  ) {
    envelope?.let {
      try {
        val message = parseMessage(body)
        onMessage?.let { handler ->
          runBlocking { handler(message) } // Process the message
        }
        channel.basicAck(envelope.deliveryTag, false)
      } catch (e: Exception) {
        channel.basicNack(envelope.deliveryTag, false, true) // Requeue message on failure
        logger.error("Error processing message: ${e.message}", e)
      }
    } ?: logger.error("Received null envelope, message discarded.")
  }

  private fun parseMessage(body: ByteArray?): ExecutorMessage {
    val messageJson =
      body?.toString(Charsets.UTF_8)
        ?: throw IllegalArgumentException("Message body is null")
    return json.decodeFromString<ExecutorMessage>(messageJson)
  }
}
