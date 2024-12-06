package karya.connectors.slack

import com.slack.api.Slack
import karya.core.actors.Connector
import karya.core.entities.Action

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
  fun build(configMap: Map<*, *>): Connector<Action.SlackMessageRequest> {
    val config = SlackConnectorConfig(configMap)
    val slack = Slack.getInstance()
    val method = slack.methods(config.token)
    return SlackConnector(method, slack)
  }
}
