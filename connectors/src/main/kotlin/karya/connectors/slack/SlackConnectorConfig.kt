package karya.connectors.slack

import karya.core.utils.readValue

/**
 * Configuration data class for SlackConnector.
 *
 * @property token The token used for authenticating with the Slack API.
 */
data class SlackConnectorConfig(
  val token: String
) {
  companion object {
    /**
     * Identifier for the SlackConnectorConfig.
     */
    const val IDENTIFIER = "slack"
  }

  /**
   * Secondary constructor to create an instance of SlackConnectorConfig from a configuration map.
   *
   * @param configMap The map containing configuration values.
   */
  constructor(configMap: Map<*, *>) : this(
    token = configMap.readValue("token"),
  )
}
