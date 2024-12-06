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

/**
 * Connector implementation for handling REST API requests.
 *
 * @property httpClient The HTTP client used for making requests.
 */
class RestApiConnector(
  private val httpClient: HttpClient,
) : Connector<RestApiRequest> {

  /**
   * Invokes the REST API request with the given plan ID and action.
   *
   * @param planId The ID of the plan.
   * @param action The action containing the REST API request details.
   * @return The result of the execution.
   */
  override suspend fun invoke(planId: UUID, action: RestApiRequest): ExecutorResult = try {
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

  /**
   * Shuts down the connector and releases any resources.
   */
  override suspend fun shutdown() {
    httpClient.close()
  }

  /**
   * Builds the URL for the REST API request.
   *
   * @param action The action containing the REST API request details.
   * @return The constructed URL.
   */
  private fun buildUrl(action: RestApiRequest) = "${action.protocol.name.lowercase()}://${action.baseUrl}"

  /**
   * Maps the custom HTTP method to Ktor's HttpMethod.
   *
   * @param method The custom HTTP method.
   * @return The corresponding Ktor HttpMethod.
   */
  private fun mapMethod(method: Method) =
    when (method) {
      Method.GET -> HttpMethod.Get
      Method.PUT -> HttpMethod.Put
      Method.POST -> HttpMethod.Post
      Method.PATCH -> HttpMethod.Patch
      Method.DELETE -> HttpMethod.Delete
    }

  /**
   * Sets the request body for the HTTP request.
   *
   * @param body The body of the request.
   */
  private fun HttpRequestBuilder.setRequestBody(body: Body) {
    when (body) {
      is Body.JsonBody -> setBody(body.jsonString)
      is Body.EmptyBody -> Unit
    }
  }

  /**
   * Handles the HTTP response and returns the appropriate ExecutorResult.
   *
   * @param response The HTTP response.
   * @param action The action containing the REST API request details.
   * @param timestamp The timestamp of the execution.
   * @return The result of the execution.
   */
  private suspend fun handleResponse(response: HttpResponse, action: RestApiRequest, timestamp: Long): ExecutorResult =
    if (response.status.isSuccess()) {
      ExecutorResult.Success(timestamp)
    } else {
      val message =
        "REST call failed --- [${response.status.value} | ${response.request.url}] | ${response.bodyAsText()}"
      ExecutorResult.Failure(message, action, timestamp)
    }
}
