package karya.connectors.email.configs

import java.util.*

data class EmailConnectorConfig(
  val username: String,
  val password: String,
  val smtpProperties: Properties
) {
  companion object {
    const val IDENTIFIER = "email"
  }
}
