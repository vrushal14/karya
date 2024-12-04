package karya.connectors.slackmessage.configs

data class SlackMessageConnectorConfig(
  val token: String
) {
  companion object {
    const val IDENTIFIER = "slackmessage"
  }
}
