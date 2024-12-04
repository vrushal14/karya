package karya.client.ktor.utils

import io.ktor.client.statement.*
import karya.client.exceptions.KaryaClientException.*
import kotlinx.serialization.json.Json

/**
 * Extension function to deserialize an HTTP response to a specified type.
 *
 * This function reads the response body as text, checks the status code,
 * and deserializes the response body to the specified type if the status code indicates success.
 * If the status code indicates a client or server error, it throws an appropriate exception.
 *
 * @param json The JSON serializer instance.
 * @return The deserialized response body as the specified type.
 * @throws KaryaServer4xxException If the status code is in the 400-499 range.
 * @throws KaryaServer5xxException If the status code is in the 500-599 range.
 * @throws KaryaServerUnknownException If the status code is outside the 200-299, 400-499, and 500-599 ranges.
 */
suspend inline fun <reified Success> HttpResponse.deserialize(json: Json): Success {
  val text = bodyAsText()
  val statusCode = status.value
  val url = call.request.url.toString()

  return when (statusCode) {
    in 200..299 -> json.decodeFromString<Success>(text)
    in 400..499 -> throw KaryaServer4xxException(text)
    in 500..599 -> throw KaryaServer5xxException(url, statusCode, text)

    else -> throw KaryaServerUnknownException(url, statusCode, text)
  }
}
