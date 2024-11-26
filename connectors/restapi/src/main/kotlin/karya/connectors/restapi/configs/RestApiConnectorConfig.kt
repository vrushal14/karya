package karya.connectors.restapi.configs

data class RestApiConnectorConfig(
  val keepAliveTime: Long,
  val connectionTimeout: Long,
  val connectionAttempts: Int,
) {
  companion object {
    const val IDENTIFIER = "restapi"
  }
}
