package karya.client.exceptions

import karya.core.exceptions.KaryaException
import kotlinx.serialization.Serializable

@Serializable
sealed class KaryaClientException : KaryaException() {

  @Serializable
  data class KaryaServer4xxException(
    override val message: String
  ) : KaryaClientException()

  @Serializable
  data class KaryaServer5xxException(
    private val url: String,
    private val statusCode: Int,
    private val text: String,
    override val message: String = "Server responded with --- [$url] --- [$statusCode | $text]"
  ) : KaryaClientException()

}
