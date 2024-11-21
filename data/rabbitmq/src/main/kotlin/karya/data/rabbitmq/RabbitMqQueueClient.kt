package karya.data.rabbitmq

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import karya.core.queues.QueueClient
import karya.core.queues.entities.ExecutorMessage
import org.apache.logging.log4j.kotlin.Logging

class RabbitMqQueueClient(
	private val channel: Channel,
	private val connection: Connection,
) : QueueClient {
	companion object : Logging {
		private const val EXCHANGE_NAME = "karya-exchange"
		private const val EXCHANGE_TYPE = "direct"
		private const val EXECUTOR_QUEUE_NAME = "karya-executor-queue"
		private const val ROUTING_KEY = "task-executor"
	}

	init {
		try {
			channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, true)
			channel.queueDeclare(EXECUTOR_QUEUE_NAME, true, false, false, null)
			channel.queueBind(EXECUTOR_QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY)
		} catch (e: Exception) {
			logger.error("Error during RabbitMQ initialization: ${e.message}", e)
			throw e // Optionally rethrow to prevent startup if configuration is critical
		}
	}

	override suspend fun push(message: ExecutorMessage) {
		logger.info("[TASK PUSHED] --- message pushed to $EXCHANGE_NAME : $message")
	}

	override suspend fun shutdown(): Boolean =
		try {
			channel.close()
			connection.close()
			logger.info("RabbitMqQueueClient shutdown successfully")
			true
		} catch (e: Exception) {
			logger.error("Error shutting down RabbitMqQueueClient --- $e")
			false
		}
}
