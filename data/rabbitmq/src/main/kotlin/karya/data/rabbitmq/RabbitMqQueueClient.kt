package karya.data.rabbitmq

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import karya.core.queues.QueueClient
import karya.core.queues.entities.ExecutorMessage
import karya.core.queues.entities.QueueType
import karya.data.rabbitmq.configs.ExchangeConfig.DL_EXCHANGE_NAME
import karya.data.rabbitmq.configs.ExchangeConfig.DL_ROUTING_KEY
import karya.data.rabbitmq.configs.ExchangeConfig.EXCHANGE_NAME
import karya.data.rabbitmq.configs.ExchangeConfig.QUEUE_NAME
import karya.data.rabbitmq.configs.ExchangeConfig.ROUTING_KEY
import karya.data.rabbitmq.usecases.external.InitializeConfiguration
import karya.data.rabbitmq.usecases.external.RabbitMqConsumer
import karya.data.rabbitmq.usecases.internal.MessageEncoder
import karya.data.rabbitmq.usecases.internal.MessagePropertiesBuilder
import kotlinx.serialization.json.Json
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class RabbitMqQueueClient
@Inject
constructor(
  private val channel: Channel,
  private val connection: Connection,
  private val consumer: RabbitMqConsumer,
  private val messageEncoder: MessageEncoder,
  private val messagePropertiesBuilder: MessagePropertiesBuilder,

  initializeConfiguration: InitializeConfiguration,
) : QueueClient {

  companion object : Logging {
    val json = Json {
      ignoreUnknownKeys = true
      isLenient = true
      prettyPrint = false
    }
  }

  init {
    initializeConfiguration.invoke()
  }

  override suspend fun push(message: ExecutorMessage, queueType: QueueType) {
    try {
      val messageBytes = messageEncoder.encode(message)
      val properties = messagePropertiesBuilder.build()
      val (exchangeToUse, routingKeyToUse) = provideExchangeAndRoutingKey(queueType)
      channel.basicPublish(exchangeToUse, routingKeyToUse, properties, messageBytes)
      logger.info("[TASK PUSHED] --- message pushed to $exchangeToUse : $message")

    } catch (e: Exception) {
      logger.error("Error pushing message to RabbitMQ: ${e.message}", e)
      throw e
    }
  }

  // will maintain a persistent connection so no need for polling
  override suspend fun consume(onMessage: suspend (ExecutorMessage) -> Unit) {
    consumer.onMessage = onMessage
    channel.basicConsume(QUEUE_NAME, false, consumer)
    logger.info("Started consuming messages from queue: $QUEUE_NAME")
  }

  override suspend fun shutdown(): Boolean = try {
    channel.close()
    connection.close()
    logger.info("RabbitMqQueueClient shutdown successfully")
    true
  } catch (e: Exception) {
    logger.error("Error shutting down RabbitMqQueueClient --- $e")
    false
  }

  private fun provideExchangeAndRoutingKey(queueType: QueueType): Pair<String, String> = when (queueType) {
    QueueType.REGULAR -> Pair(EXCHANGE_NAME, ROUTING_KEY)
    QueueType.DEAD_LETTER -> Pair(DL_EXCHANGE_NAME, DL_ROUTING_KEY)
  }
}
