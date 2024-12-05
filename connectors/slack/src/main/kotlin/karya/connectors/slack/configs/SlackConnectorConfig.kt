package karya.connectors.slack.configs

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
}
