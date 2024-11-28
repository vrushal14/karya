package karya.core.entities.action

import karya.core.entities.action.http.Body
import karya.core.entities.action.http.Method
import karya.core.entities.action.http.Protocol
import karya.core.entities.requests.SubmitJobRequest
import karya.core.exceptions.JobException.RecurisveDepthExceededException
import kotlinx.serialization.Serializable

@Serializable
sealed class Action {

  companion object {
    private const val MAX_CHAINED_RECURSION_DEPTH = 2
  }

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
  data class ChainedRequest(
    val request: SubmitJobRequest
  ) : Action() {
    init {
      var depth = 1
      var currentAction: Action = this
      while (currentAction is ChainedRequest) {
        if (depth > MAX_CHAINED_RECURSION_DEPTH) {
          throw RecurisveDepthExceededException(depth, MAX_CHAINED_RECURSION_DEPTH)
        }
        depth++
        currentAction = currentAction.request.action
      }
    }
  }
}
