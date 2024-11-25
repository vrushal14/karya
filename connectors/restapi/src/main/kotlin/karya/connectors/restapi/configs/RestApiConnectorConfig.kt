package karya.connectors.restapi.configs

data class RestApiConnectorConfig(
  val keepAliveTime: Long,
  val connectionTimeout: Long,
  val connectionAttempt: Int,
)
