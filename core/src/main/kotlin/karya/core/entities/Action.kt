package karya.core.entities

import karya.core.entities.http.Body
import karya.core.entities.http.Method
import karya.core.entities.http.Protocol
import karya.core.entities.requests.SubmitPlanRequest
import kotlinx.serialization.Serializable

@Serializable
sealed class Action {

  @Serializable
  data class RestApiRequest(
    val protocol: Protocol = Protocol.HTTP,
    val baseUrl: String,
    val method: Method = Method.GET,
    val headers: Map<String, String> = mapOf("content-type" to "application/json"),
    val body: Body = Body.EmptyBody,
    val timeout: Long = 2000L,
  ) : Action()

  @Serializable
  data class SlackMessageRequest(
    val message: String,
    val channel: String
  ) : Action()

  @Serializable
  data class ChainedRequest(
    val request: SubmitPlanRequest
  ) : Action()
}
