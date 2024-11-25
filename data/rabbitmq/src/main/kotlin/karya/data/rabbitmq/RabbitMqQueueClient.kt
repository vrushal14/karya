package karya.data.rabbitmq

import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import karya.core.queues.QueueClient
import karya.core.queues.entities.ExecutorMessage
import kotlinx.serialization.json.Json
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class RabbitMqQueueClient
@Inject
constructor(
    private val channel: Channel,
    private val connection: Connection,
    private val consumer: RabbitMqConsumer,
) : QueueClient {
    companion object : Logging {
        private const val EXCHANGE_NAME = "karya-exchange"
        private const val EXCHANGE_TYPE = "direct"
        private const val EXECUTOR_QUEUE_NAME = "karya-executor-queue"
        private const val ROUTING_KEY = "task-executor"
        private const val DELIVERY_MODE = 2 // persistent
        private const val PRIORITY_LEVEL = 1

        val json =
            Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = false
            }
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
        try {
            val messageBytes = createPayload(message)
            val properties = createProperties()
            channel.basicPublish(
                EXCHANGE_NAME,
                ROUTING_KEY,
                properties,
                messageBytes,
            )
            logger.info("[TASK PUSHED] --- message pushed to $EXCHANGE_NAME : $message")
        } catch (e: Exception) {
            logger.error("Error pushing message to RabbitMQ: ${e.message}", e)
            throw e
        }
    }

    // will maintain a persistent connection so no need for polling
    override suspend fun consume(onMessage: suspend (ExecutorMessage) -> Unit) {
        consumer.onMessage = onMessage
        channel.basicConsume(EXECUTOR_QUEUE_NAME, false, consumer)
        logger.info("Started consuming messages from queue: $EXECUTOR_QUEUE_NAME")
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

    private fun createPayload(message: ExecutorMessage) =
        json
            .encodeToString(ExecutorMessage.serializer(), message)
            .toByteArray(Charsets.UTF_8)

    private fun createProperties() =
        BasicProperties
            .Builder()
            .contentType("application/json")
            .deliveryMode(DELIVERY_MODE)
            .priority(PRIORITY_LEVEL)
            .build()
}
