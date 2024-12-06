package karya.connectors.restapi

import karya.core.utils.readValue

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

  /**
   * Secondary constructor to create an instance of RestApiConnectorConfig from a configuration map.
   *
   * @param configMap The map containing configuration values.
   */
  constructor(configMap: Map<*, *>) : this(
    keepAliveTime = configMap.readValue("keepAliveTime"),
    connectionTimeout = configMap.readValue("connectionTimeout"),
    connectionAttempts = configMap.readValue("connectionAttempts"),
  )
}
