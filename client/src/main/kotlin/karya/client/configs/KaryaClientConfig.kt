package karya.client.configs

import karya.core.entities.http.Protocol

/**
 * Configuration class for the Karya client.
 *
 * @property protocol The protocol to be used (HTTP or HTTPS).
 * @property host The host address of the server.
 * @property port The port number of the server.
 * @property keepAliveTime The keep-alive time for the connection in milliseconds.
 * @property connectionTimeout The connection timeout duration in milliseconds.
 * @property connectionAttempts The number of connection attempts.
 */
data class KaryaClientConfig(
  val protocol: Protocol,
  val host: String,
  val port: Int,
  val keepAliveTime: Long,
  val connectionTimeout: Long,
  val connectionAttempts: Int,
) {
  companion object {
    /**
     * Development configuration for the Karya client.
     */
    val Dev = KaryaClientConfig(
      protocol = Protocol.HTTP,
      host = "localhost",
      port = 8080,
      keepAliveTime = 5000L,
      connectionTimeout = 5000L,
      connectionAttempts = 5
    )
  }
}
