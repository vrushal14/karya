package karya.connectors.email

import karya.core.utils.extractProperties
import karya.core.utils.readValue
import java.util.*

/**
 * Configuration class for the EmailConnector.
 *
 * @property username The username for the email account.
 * @property password The password for the email account.
 * @property smtpProperties The SMTP properties for the email session.
 */
data class EmailConnectorConfig(
  val username: String,
  val password: String,
  val smtpProperties: Properties
) {
  companion object {
    /**
     * Identifier for the email connector configuration.
     */
    const val IDENTIFIER = "email"
  }

  /**
   * Secondary constructor to create an instance of EmailConnectorConfig from a configuration map.
   *
   * @param configMap The map containing configuration values.
   */
  constructor(configMap: Map<*, *>) : this(
    username = configMap.readValue("username"),
    password = configMap.readValue("password"),
    smtpProperties = extractProperties(configMap, "smtp")
  )
}
