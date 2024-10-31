package karya.client.ktor.utils

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.statement.*
import karya.client.exceptions.KaryaServerException
import karya.core.exceptions.KaryaException

suspend inline fun <reified Success, reified Failure : Exception> HttpResponse.deserialize(
  objectMapper: ObjectMapper
): Success {

  val text = bodyAsText()
  val statusCode = status.value
  val url = call.request.url.toString()

  when(statusCode) {

    in 200..299 -> {
      val valueType = object : TypeReference<Success>() {}
      return objectMapper.readValue(text, valueType)
    }

    in 400..499 -> {
      val valueType = object : TypeReference<Failure>() {}
      throw objectMapper.readValue(text, valueType)
    }

    in 500..599 -> throw KaryaServerException(url, statusCode, text)
    else -> throw KaryaException("Unknown Exception")
  }

}