package karya.connectors.slackmessage.configs

/**
 * Configuration data class for SlackMessageConnector.
 *
 * @property token The token used for authenticating with the Slack API.
 */
data class SlackMessageConnectorConfig(
  val token: String
) {
  companion object {
    /**
     * Identifier for the SlackMessageConnectorConfig.
     */
    const val IDENTIFIER = "slackmessage"
  }
}
