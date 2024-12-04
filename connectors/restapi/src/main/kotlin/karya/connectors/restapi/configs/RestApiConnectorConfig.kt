package karya.connectors.restapi.configs

/**
 * Configuration data class for RestApiConnector.
 *
 * @property keepAliveTime The time to keep the connection alive.
 * @property connectionTimeout The timeout duration for the connection.
 * @property connectionAttempts The number of attempts to establish a connection.
 */
data class RestApiConnectorConfig(
  val keepAliveTime: Long,
  val connectionTimeout: Long,
  val connectionAttempts: Int,
) {
  companion object {
    /**
     * Identifier for the RestApiConnectorConfig.
     */
    const val IDENTIFIER = "restapi"
  }
}
