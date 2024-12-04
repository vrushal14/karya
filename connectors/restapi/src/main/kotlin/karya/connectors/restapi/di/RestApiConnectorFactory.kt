package karya.connectors.restapi.di

import karya.connectors.restapi.configs.RestApiConnectorConfig
import karya.core.utils.readValue

/**
 * Factory object for creating instances of RestApiConnector.
 */
object RestApiConnectorFactory {

  /**
   * Builds a RestApiConnector using the provided configuration map.
   *
   * @param configMap The map containing configuration values.
   * @return An instance of RestApiConnector.
   */
  fun build(configMap: Map<*, *>) =
    RestApiConnectorConfig(
      keepAliveTime = configMap.readValue("keepAliveTime"),
      connectionTimeout = configMap.readValue("connectionTimeout"),
      connectionAttempts = configMap.readValue("connectionAttempts"),
    ).let(::build)

  /**
   * Builds a RestApiConnector using the provided RestApiConnectorConfig.
   *
   * @param config The configuration for the RestApiConnector.
   * @return An instance of RestApiConnector.
   */
  private fun build(config: RestApiConnectorConfig) =
    DaggerRestApiConnectorComponent.builder()
      .config(config)
      .build()
      .connector
}
