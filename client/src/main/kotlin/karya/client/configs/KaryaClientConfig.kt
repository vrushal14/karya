package karya.client.configs

import io.ktor.http.*

data class KaryaClientConfig(
	val protocol: URLProtocol,
	val host: String,
	val port: Int,
) {
	companion object {
		val Dev =
			KaryaClientConfig(
				protocol = URLProtocol.HTTP,
				host = "localhost",
				port = 8080,
			)
	}
}
