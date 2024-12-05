package karya.connectors.kafka.configs

data class KafkaConnectorConfig(
  val bootstrapServers: String,
) {
  companion object {
    const val IDENTIFIER = "kafka"
  }
}
