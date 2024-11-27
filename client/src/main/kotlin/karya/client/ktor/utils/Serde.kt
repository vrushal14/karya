package karya.client.ktor.utils

import io.ktor.client.statement.*
import karya.client.exceptions.KaryaClientException.*
import kotlinx.serialization.json.Json

suspend inline fun <reified Success> HttpResponse.deserialize(json: Json): Success {
  val text = bodyAsText()
  val statusCode = status.value
  val url = call.request.url.toString()

  return when (statusCode) {
    in 200..299 -> json.decodeFromString<Success>(text)
    in 400..499 -> throw KaryaServer4xxException(text)
    in 500..599 -> throw KaryaServer5xxException(url, statusCode, text)

    else -> throw RuntimeException("Unhandled status code : $statusCode | response : $text")
  }
}
