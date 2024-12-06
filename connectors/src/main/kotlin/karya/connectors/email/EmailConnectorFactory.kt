package karya.connectors.email

import karya.core.actors.Connector
import karya.core.entities.Action
import javax.mail.PasswordAuthentication
import javax.mail.Session

/**
 * Factory object for creating instances of EmailConnectorConfig and EmailConnector.
 */
object EmailConnectorFactory {

  /**
   * Builds an EmailConnector using the provided configuration map.
   *
   * @param configMap The map containing configuration values.
   * @return An instance of EmailConnector.
   */
  fun build(configMap: Map<*, *>): Connector<Action.EmailRequest> {
    val config = EmailConnectorConfig(configMap)
    val session = provideSession(config)
    return EmailConnector(session, config)
  }

  /**
   * Provides a JavaMail session using the provided EmailConnectorConfig.
   *
   * @param config The configuration for the EmailConnector.
   * @return A JavaMail session.
   */
  private fun provideSession(config: EmailConnectorConfig): Session =
    Session.getInstance(config.smtpProperties, object : javax.mail.Authenticator() {
      override fun getPasswordAuthentication(): PasswordAuthentication {
        return PasswordAuthentication(config.username, config.password)
      }
    })
}
