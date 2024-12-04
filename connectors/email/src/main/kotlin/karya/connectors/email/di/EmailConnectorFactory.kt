package karya.connectors.email.di

import karya.connectors.email.configs.EmailConnectorConfig
import karya.core.utils.extractProperties
import karya.core.utils.readValue

/**
 * Factory object for creating instances of EmailConnectorConfig and EmailConnector.
 */
object EmailConnectorFactory {

  /**
   * Builds an EmailConnectorConfig from a configuration map and then creates an EmailConnector.
   *
   * @param configMap The map containing configuration values.
   * @return The created EmailConnector instance.
   */
  fun build(configMap: Map<*,*>) = EmailConnectorConfig(
    username = configMap.readValue("username"),
    password = configMap.readValue("password"),
    smtpProperties = extractProperties(configMap, "smtp")
  ).let(::build)

  /**
   * Builds an EmailConnector using the provided EmailConnectorConfig.
   *
   * @param config The configuration for the EmailConnector.
   * @return The created EmailConnector instance.
   */
  private fun build(config: EmailConnectorConfig) =
    DaggerEmailConnectorComponent.builder()
      .emailConnectorConfig(config)
      .build()
      .connector
}
