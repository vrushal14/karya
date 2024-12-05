package karya.connectors.kafka.di

import karya.connectors.kafka.configs.KafkaConnectorConfig
import karya.core.utils.readValue

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
  fun build(configMap: Map<*, *>) = KafkaConnectorConfig(
    bootstrapServers = configMap.readValue("bootstrap.servers"),
  ).let(::build)

  /**
   * Builds a KafkaConnector using the provided KafkaConnectorConfig.
   *
   * @param config The configuration for the KafkaConnector.
   * @return An instance of KafkaConnector.
   */
  private fun build(config: KafkaConnectorConfig) =
    DaggerKafkaConnectorComponent.builder()
      .kafkaConnectorConfig(config)
      .build()
      .connector
}
