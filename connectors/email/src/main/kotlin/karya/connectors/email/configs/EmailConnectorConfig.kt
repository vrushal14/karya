package karya.connectors.email.configs

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
}
