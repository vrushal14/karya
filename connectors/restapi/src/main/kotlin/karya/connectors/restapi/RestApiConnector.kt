package karya.connectors.restapi

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import karya.core.actors.Connector
import karya.core.entities.Action.RestApiRequest
import karya.core.entities.ExecutorResult
import karya.core.entities.http.Body
import karya.core.entities.http.Method
import java.time.Instant
import java.util.*
import javax.inject.Inject

class RestApiConnector
@Inject
constructor(
  private val httpClient: HttpClient,
) : Connector<RestApiRequest> {

  override suspend fun invoke(jobId: UUID, action: RestApiRequest): ExecutorResult = try {
    val response = httpClient.request(buildUrl(action)) {
      method = mapMethod(action.method)
      headers { action.headers.forEach { key, value -> append(key, value) } }
      timeout { requestTimeoutMillis = action.timeout }
      setRequestBody(action.body)
    }
    handleResponse(response, action, Instant.now().toEpochMilli())

  } catch (e: Exception) {
    val message = "REST INVOCATION FAILED --- error : ${e.message}"
    ExecutorResult.Failure(message, action, Instant.now().toEpochMilli())
  }

  override suspend fun shutdown() {
    httpClient.close()
  }

  private fun buildUrl(action: RestApiRequest) = "${action.protocol.name.lowercase()}://${action.baseUrl}"

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

  private suspend fun handleResponse(response: HttpResponse, action: RestApiRequest, timestamp: Long): ExecutorResult =
    if (response.status.isSuccess()) {
      ExecutorResult.Success(timestamp)
    } else {
      val message =
        "REST call failed --- [${response.status.value} | ${response.request.url}] | ${response.bodyAsText()}"
      ExecutorResult.Failure(message, action, timestamp)
    }
}
