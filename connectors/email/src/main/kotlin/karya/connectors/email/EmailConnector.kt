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

/**
 * Connector implementation for sending emails using JavaMail.
 *
 * @property session The JavaMail session used for sending emails.
 * @property config The configuration for the EmailConnector.
 */
class EmailConnector
@Inject
constructor(
  private val session: Session,
  private val config: EmailConnectorConfig
) : Connector<Action.EmailRequest> {

  /**
   * Invokes the email sending action.
   *
   * @param planId The unique identifier of the plan.
   * @param action The email request action containing recipient, subject, and message.
   * @return The result of the email sending action.
   */
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

  /**
   * Shuts down the connector. No explicit resource to close for JavaMail session.
   */
  override suspend fun shutdown() {
    // No explicit resource to close for JavaMail session
  }

  /**
   * Creates a MimeMessage from the email request action.
   *
   * @param action The email request action containing recipient, subject, and message.
   * @return The created MimeMessage.
   */
  private fun createMimeMessage(action: Action.EmailRequest) = MimeMessage(session).apply {
    setFrom(InternetAddress(config.username))
    setRecipients(Message.RecipientType.TO, InternetAddress.parse(action.recipient))
    subject = action.subject
    setText(action.message)
  }
}
