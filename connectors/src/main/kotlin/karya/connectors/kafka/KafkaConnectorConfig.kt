package karya.connectors.kafka

import karya.core.utils.readValue

/**
 * Configuration class for the KafkaConnector.
 *
 * @property bootstrapServers The Kafka bootstrap servers.
 */
data class KafkaConnectorConfig(
  val bootstrapServers: String,
) {
  companion object {
    /**
     * Identifier for the Kafka connector configuration.
     */
    const val IDENTIFIER = "kafka"
  }

  /**
   * Secondary constructor to create an instance of KafkaConnectorConfig from a configuration map.
   *
   * @param configMap The map containing configuration values.
   */
  constructor(configMap: Map<*, *>) : this(
    bootstrapServers = configMap.readValue("bootstrap.servers")
  )
}
