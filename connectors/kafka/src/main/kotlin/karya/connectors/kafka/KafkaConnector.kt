package karya.connectors.kafka

import karya.core.actors.Connector
import karya.core.entities.Action
import karya.core.entities.ExecutorResult
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.time.Instant
import java.util.*
import javax.inject.Inject

/**
 * Connector implementation for sending messages to Kafka topic.
 *
 * @property kafkaProducer The Kafka producer used to send messages.
 */
class KafkaConnector
@Inject
constructor(
  private val kafkaProducer: KafkaProducer<String, String>
) : Connector<Action.KafkaProducerRequest> {

  /**
   * Sends a message to the specified Kafka topic.
   *
   * @param planId The ID of the plan.
   * @param action The action containing the Kafka request details.
   * @return The result of the execution.
   */
  override suspend fun invoke(planId: UUID, action: Action.KafkaProducerRequest): ExecutorResult = try {
    val record = ProducerRecord(action.topic, action.key, action.message)
    kafkaProducer.send(record).get()
    ExecutorResult.Success(Instant.now().toEpochMilli())

  } catch (e: Exception) {
    ExecutorResult.Failure(
      reason = e.message ?: e.localizedMessage,
      action = action,
      timestamp = Instant.now().toEpochMilli()
    )
  }

  override suspend fun shutdown() {
    kafkaProducer.close()
  }
}
