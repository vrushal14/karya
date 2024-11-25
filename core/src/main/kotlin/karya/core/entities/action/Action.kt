package karya.core.entities.action

import karya.core.entities.action.http.Body
import karya.core.entities.action.http.Method
import karya.core.entities.action.http.Protocol
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
}
