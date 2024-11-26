package karya.connectors.restapi.configs

data class RestApiConnectorConfig(
  val keepAliveTime: Long,
  val connectionTimeout: Long,
  val connectionAttempt: Int,
) {
  companion object {
    const val REST_API_CONNECTOR_IDENTIFIER = "restapi"
  }
}
