package karya.connectors.kafka

import karya.core.actors.Connector
import karya.core.entities.Action
import org.apache.kafka.clients.producer.KafkaProducer
import java.util.*

/**
 * Factory object for creating instances of KafkaConnector.
 */
object KafkaConnectorFactory {

  /**
   * Builds a KafkaConnector using the provided configuration map.
   *
   * @param configMap The map containing configuration values.
   * @return An instance of KafkaConnector.
   */
  fun build(configMap: Map<*, *>): Connector<Action.KafkaProducerRequest> {
    val config = KafkaConnectorConfig(configMap)
    val producer = provideKafkaProducer(config)
    return KafkaConnector(producer)
  }

  /**
   * Provides a KafkaProducer using the provided KafkaConnectorConfig.
   *
   * @param config The configuration for the KafkaConnector.
   * @return An instance of KafkaProducer.
   */
  fun provideKafkaProducer(config: KafkaConnectorConfig): KafkaProducer<String, String> {
    val props = Properties()
    props["bootstrap.servers"] = config.bootstrapServers
    props["key.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
    props["value.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
    return KafkaProducer(props)
  }
}
