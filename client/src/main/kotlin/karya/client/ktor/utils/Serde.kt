package karya.client.ktor.utils

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import karya.client.exceptions.KaryaServerException
import karya.core.exceptions.KaryaException
import kotlinx.serialization.json.Json

suspend inline fun <reified Success, reified Failure : Exception> HttpResponse.deserialize(json: Json): Success {
    val text = bodyAsText()
    val statusCode = status.value
    val url = call.request.url.toString()

    return when (statusCode) {
        in 200..299 -> json.decodeFromString<Success>(text)

        in 400..499 -> {
            val failureResponse = json.decodeFromString<Failure>(text)
            throw failureResponse
        }

        in 500..599 -> throw KaryaServerException(url, statusCode, text)
        else -> throw KaryaException("Unknown Exception")
    }
}
