package karya.client.configs

import karya.core.entities.action.http.Protocol

data class KaryaClientConfig(
  val protocol: Protocol,
  val host: String,
  val port: Int,

  val keepAliveTime: Long,
  val connectionTimeout: Long,
  val connectionAttempts: Int,
) {
  companion object {
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
