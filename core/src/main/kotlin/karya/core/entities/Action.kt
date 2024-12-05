package karya.core.entities

import karya.core.entities.http.Body
import karya.core.entities.http.Method
import karya.core.entities.http.Protocol
import karya.core.entities.requests.SubmitPlanRequest
import kotlinx.serialization.Serializable

/**
 * Sealed class representing different types of actions.
 */
@Serializable
sealed class Action {

  /**
   * Data class representing a REST API request action.
   *
   * @property protocol The protocol to be used for the request (default is HTTP).
   * @property baseUrl The base URL for the request.
   * @property method The HTTP method to be used for the request (default is GET).
   * @property headers The headers to be included in the request (default is content-type: application/json).
   * @property body The body of the request (default is an empty body).
   * @property timeout The timeout for the request in milliseconds (default is 2000ms).
   */
  @Serializable
  data class RestApiRequest(
    val protocol: Protocol = Protocol.HTTP,
    val baseUrl: String,
    val method: Method = Method.GET,
    val headers: Map<String, String> = mapOf("content-type" to "application/json"),
    val body: Body = Body.EmptyBody,
    val timeout: Long = 2000L,
  ) : Action()

  /**
   * Data class representing a Slack message request action.
   *
   * @property channel The Slack channel where the message will be sent.
   * @property message The message to be sent to the Slack channel. This has to be in the slack block kit format.
   */
  @Serializable
  data class SlackMessageRequest(
    val channel: String,
    val message: String
  ) : Action()

  /**
   * Data class representing an Email request action.
   *
   * @property recipient Email ID to whom the email is being sent to.
   * @property subject Subject of the email being sent.
   * @property message The message to be sent in the email content.
   */
  @Serializable
  data class EmailRequest(
    val recipient: String,
    val subject: String,
    val message: String
  ) : Action()

  /**
   * Data class representing a Kafka request action.
   *
   * @property key The key to be used to specify the partition to which the message will be sent.
   * @property topic The Kafka topic where the message will be sent.
   * @property message The message to be sent to the Kafka topic.
   */
  @Serializable
  data class KafkaProducerRequest(
    val key: String? = null,
    val topic: String,
    val message: String
  ) : Action()

  /**
   * Data class representing a chained request action.
   *
   * @property request The request to be chained.
   */
  @Serializable
  data class ChainedRequest(
    val request: SubmitPlanRequest
  ) : Action()
}
