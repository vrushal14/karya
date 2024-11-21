package karya.client.di

import karya.client.configs.KaryaClientConfig

object KaryaClientFactory {
	fun create(config: KaryaClientConfig) =
		DaggerKaryaClientComponent
			.builder()
			.clientConfig(config)
			.build()
			.client
}
