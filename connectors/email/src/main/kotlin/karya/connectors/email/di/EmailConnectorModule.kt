package karya.connectors.email.di

import dagger.Module
import dagger.Provides
import karya.connectors.email.EmailConnector
import karya.connectors.email.configs.EmailConnectorConfig
import karya.core.actors.Connector
import karya.core.entities.Action
import javax.inject.Singleton
import javax.mail.PasswordAuthentication
import javax.mail.Session

@Module
class EmailConnectorModule {

  @Provides
  @Singleton
  fun provideEmailConnector(session: Session, config: EmailConnectorConfig): Connector<Action.EmailRequest> =
    EmailConnector(session, config)

  @Provides
  @Singleton
  fun provideSession(config: EmailConnectorConfig): Session =
    Session.getInstance(config.smtpProperties, object : javax.mail.Authenticator() {
      override fun getPasswordAuthentication(): PasswordAuthentication {
        return PasswordAuthentication(config.username, config.password)
      }
    })
}