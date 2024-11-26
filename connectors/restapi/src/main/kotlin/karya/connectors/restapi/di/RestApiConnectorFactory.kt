package karya.connectors.restapi.di

import karya.connectors.restapi.configs.RestApiConnectorConfig
import karya.core.utils.readValue

object RestApiConnectorFactory {

  fun build(configMap: Map<*, *>) =
    RestApiConnectorConfig(
      keepAliveTime = configMap.readValue("keepAliveTime"),
      connectionTimeout = configMap.readValue("connectionTimeout"),
      connectionAttempts = configMap.readValue("connectionAttempts"),
    ).let { build(it) }

  fun build(config: RestApiConnectorConfig) =
    DaggerRestApiConnectorComponent.builder()
      .config(config)
      .build()
      .connector
}
