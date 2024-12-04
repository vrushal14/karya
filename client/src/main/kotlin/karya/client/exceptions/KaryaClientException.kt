package karya.client.exceptions

import karya.core.exceptions.KaryaException
import kotlinx.serialization.Serializable

/**
 * Base class for exceptions specific to the Karya client.
 */
@Serializable
sealed class KaryaClientException : KaryaException() {

  /**
   * Exception class for 4xx HTTP status codes.
   *
   * @property message The error message.
   */
  @Serializable
  data class KaryaServer4xxException(
    override val message: String
  ) : KaryaClientException()

  /**
   * Exception class for 5xx HTTP status codes.
   *
   * @property url The URL that caused the exception.
   * @property statusCode The HTTP status code.
   * @property text The response body text.
   * @property message The error message.
   */
  @Serializable
  data class KaryaServer5xxException(
    private val url: String,
    private val statusCode: Int,
    private val text: String,
    override val message: String = "Server responded with --- [$url] --- [$statusCode | $text]"
  ) : KaryaClientException()

  /**
   * Exception class for unknown HTTP status codes.
   *
   * @property url The URL that caused the exception.
   * @property statusCode The HTTP status code.
   * @property text The response body text.
   * @property message The error message.
   */
  @Serializable
  data class KaryaServerUnknownException(
    private val url: String,
    private val statusCode: Int,
    private val text: String,
    override val message: String = "Server responded with --- [$url] --- [$statusCode | $text]"
  ) : KaryaClientException()
}
