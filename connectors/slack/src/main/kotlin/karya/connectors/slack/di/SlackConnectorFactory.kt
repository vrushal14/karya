package karya.connectors.slack.di

import karya.connectors.slack.configs.SlackConnectorConfig
import karya.core.utils.readValue

/**
 * Factory object for creating instances of SlackConnector.
 */
object SlackConnectorFactory {

  /**
   * Builds a SlackConnector using the provided configuration map.
   *
   * @param configMap The map containing configuration values.
   * @return An instance of SlackConnector.
   */
  fun build(configMap: Map<*, *>) = SlackConnectorConfig(
    token = configMap.readValue("token"),
  ).let(::build)

  /**
   * Builds a SlackConnector using the provided SlackConnectorConfig.
   *
   * @param config The configuration for the SlackConnector.
   * @return An instance of SlackConnector.
   */
  private fun build(config: SlackConnectorConfig) =
    DaggerSlackConnectorComponent.builder()
      .slackMessageConnectorConfig(config)
      .build()
      .connector
}
