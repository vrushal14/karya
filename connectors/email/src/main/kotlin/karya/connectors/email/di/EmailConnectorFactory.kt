package karya.connectors.email.di

import karya.connectors.email.configs.EmailConnectorConfig
import karya.core.utils.extractProperties
import karya.core.utils.readValue

object EmailConnectorFactory {

  fun build(configMap: Map<*,*>) = EmailConnectorConfig(
    username = configMap.readValue("username"),
    password = configMap.readValue("password"),
    smtpProperties = extractProperties(configMap, "smtp")
  ).let(::build)

  private fun build(config: EmailConnectorConfig) =
    DaggerEmailConnectorComponent.builder()
      .emailConnectorConfig(config)
      .build()
      .connector
}