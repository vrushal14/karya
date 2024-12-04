package karya.connectors.slackmessage.di

import karya.connectors.slackmessage.configs.SlackMessageConnectorConfig
import karya.core.utils.readValue

/**
 * Factory object for creating instances of SlackMessageConnector.
 */
object SlackMessageConnectorFactory {

  /**
   * Builds a SlackMessageConnector using the provided configuration map.
   *
   * @param configMap The map containing configuration values.
   * @return An instance of SlackMessageConnector.
   */
  fun build(configMap: Map<*, *>) =
    SlackMessageConnectorConfig(
      token = configMap.readValue("token"),
    ).let(::build)

  /**
   * Builds a SlackMessageConnector using the provided SlackMessageConnectorConfig.
   *
   * @param config The configuration for the SlackMessageConnector.
   * @return An instance of SlackMessageConnector.
   */
  private fun build(config: SlackMessageConnectorConfig) =
    DaggerSlackMessageConnectorComponent.builder()
      .config(config)
      .build()
      .connector
}
