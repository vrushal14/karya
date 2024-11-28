package karya.data.rabbitmq.usecases.external

import com.rabbitmq.client.Channel
import karya.data.rabbitmq.configs.ExchangeConfig.DL_EXCHANGE_NAME
import karya.data.rabbitmq.configs.ExchangeConfig.DL_QUEUE_NAME
import karya.data.rabbitmq.configs.ExchangeConfig.DL_ROUTING_KEY
import karya.data.rabbitmq.configs.ExchangeConfig.EXCHANGE_NAME
import karya.data.rabbitmq.configs.ExchangeConfig.EXCHANGE_TYPE
import karya.data.rabbitmq.configs.ExchangeConfig.QUEUE_NAME
import karya.data.rabbitmq.configs.ExchangeConfig.ROUTING_KEY
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class InitializeConfiguration
@Inject
constructor(
  private val channel: Channel
) {

  companion object : Logging

  fun invoke() = try {
    declareExchange()
    declareDeadLetterExchange()

  } catch (e: Exception) {
    logger.error("Error during RabbitMQ initialization: ${e.message}", e)
    throw e
  }

  private fun declareExchange() {
    channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, true)
    channel.queueDeclare(
      QUEUE_NAME, true, false, false, mapOf(
        "x-dead-letter-exchange" to DL_EXCHANGE_NAME
      )
    )
    channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY)
    logger.info("RabbitMQ exchange [$EXCHANGE_NAME] initialized.")
  }

  private fun declareDeadLetterExchange() {
    channel.exchangeDeclare(DL_EXCHANGE_NAME, EXCHANGE_TYPE, true)
    channel.queueDeclare(DL_QUEUE_NAME, true, false, false, null)
    channel.queueBind(DL_QUEUE_NAME, DL_EXCHANGE_NAME, DL_ROUTING_KEY)
    logger.info("RabbitMQ exchange [$DL_EXCHANGE_NAME] initialized.")
  }
}
