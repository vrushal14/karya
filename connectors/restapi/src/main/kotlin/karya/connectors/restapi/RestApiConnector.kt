package karya.connectors.restapi

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import karya.core.actors.Connector
import karya.core.actors.Result
import karya.core.entities.action.Action
import karya.core.entities.action.http.Body
import karya.core.entities.action.http.Method
import java.time.Instant
import javax.inject.Inject

class RestApiConnector
@Inject
constructor(
  private val httpClient: HttpClient,
) : Connector<Action.RestApiRequest> {

  override suspend fun invoke(action: Action.RestApiRequest): Result = try {
    val response =
      httpClient.request(buildUrl(action)) {
        method = mapMethod(action.method)
        headers { action.headers.forEach { key, value -> append(key, value) } }
        timeout { requestTimeoutMillis = action.timeout }
        setRequestBody(action.body)
      }
    handleResponse(response, action, Instant.now())

  } catch (e: Exception) {
    val message = "REST INVOCATION FAILED --- error : ${e.message}"
    Result.Failure(message, action, e, Instant.now())
  }

  private fun buildUrl(action: Action.RestApiRequest) = "${action.protocol.name.lowercase()}://${action.baseUrl}"

  private fun mapMethod(method: Method) =
    when (method) {
      Method.GET -> HttpMethod.Get
      Method.PUT -> HttpMethod.Put
      Method.POST -> HttpMethod.Post
      Method.PATCH -> HttpMethod.Patch
      Method.DELETE -> HttpMethod.Delete
    }

  private fun HttpRequestBuilder.setRequestBody(body: Body) {
    when (body) {
      is Body.JsonBody -> setBody(body.jsonString)
      is Body.EmptyBody -> Unit
    }
  }

  private suspend fun handleResponse(response: HttpResponse, action: Action.RestApiRequest, timestamp: Instant): Result =
    if (response.status.isSuccess()) {
      Result.Success(timestamp)
    } else {
      val message = "REST call failed with status [${response.status.value} | ${response.request.url}] | ${response.bodyAsText()}"
      Result.Failure(message, action, null, timestamp)
    }
}
