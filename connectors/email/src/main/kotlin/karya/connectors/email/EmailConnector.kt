package karya.connectors.email

import karya.connectors.email.configs.EmailConnectorConfig
import karya.core.actors.Connector
import karya.core.entities.Action
import karya.core.entities.ExecutorResult
import java.time.Instant
import java.util.*
import javax.inject.Inject
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailConnector
@Inject
constructor(
  private val session: Session,
  private val config: EmailConnectorConfig
) : Connector<Action.EmailRequest> {

  override suspend fun invoke(planId: UUID, action: Action.EmailRequest): ExecutorResult = try {
    val message = createMimeMessage(action)
    Transport.send(message)
    ExecutorResult.Success(Instant.now().toEpochMilli())

  } catch (e: Exception) {
    ExecutorResult.Failure(
      reason = e.message ?: e.localizedMessage,
      action = action,
      timestamp = Instant.now().toEpochMilli()
    )
  }

  override suspend fun shutdown() {
    // No explicit resource to close for JavaMail session
  }

  private fun createMimeMessage(action: Action.EmailRequest) = MimeMessage(session).apply {
    setFrom(InternetAddress(config.username))
    setRecipients(Message.RecipientType.TO, InternetAddress.parse(action.recipient))
    subject = action.subject
    setText(action.message)
  }
}