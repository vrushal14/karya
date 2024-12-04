package karya.connectors.slackmessage.di

import karya.connectors.slackmessage.configs.SlackMessageConnectorConfig
import karya.core.utils.readValue

object SlackMessageConnectorFactory {
  fun build(configMap: Map<*, *>) =
    SlackMessageConnectorConfig(
      token = configMap.readValue("token"),
    ).let(::build)

  private fun build(config: SlackMessageConnectorConfig) =
    DaggerSlackMessageConnectorComponent.builder()
      .config(config)
      .build()
      .connector
}
