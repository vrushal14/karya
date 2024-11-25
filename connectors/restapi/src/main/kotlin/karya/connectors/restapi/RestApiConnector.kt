package karya.connectors.restapi

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import karya.core.actors.Connector
import karya.core.actors.Result
import karya.core.entities.action.Action
import karya.core.entities.action.http.Body
import karya.core.entities.action.http.Method
import javax.inject.Inject

class RestApiConnector
@Inject
constructor(
  private val httpClient: HttpClient,
) : Connector<Action.RestApiRequest> {
  override suspend fun invoke(action: Action.RestApiRequest): Result =
    try {
      val response =
        httpClient.request(buildUrl(action)) {
          method = mapMethod(action.method)
          headers { action.headers.forEach { key, value -> append(key, value) } }
          timeout { requestTimeoutMillis = action.timeout }
          setRequestBody(action.body)
        }
      handleResponse(response, action)
    } catch (e: Exception) {
      val message = "REST INVOCATION FAILED --- error : ${e.message}"
      Result.Failure(message, action, e)
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
      is Body.EmptyBody -> {}
    }
  }

  private fun handleResponse(
    response: HttpResponse,
    action: Action.RestApiRequest,
  ): Result =
    if (response.status.isSuccess()) {
      Result.Success
    } else {
      val message = "REST call failed with status ${response.status.value}"
      Result.Failure(message, action)
    }
}
