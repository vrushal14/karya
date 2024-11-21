package karya.servers.scheduler.di.factories

import karya.servers.scheduler.configs.SchedulerConfig
import karya.servers.scheduler.di.components.DaggerSchedulerFetcherComponent

object SchedulerFetcherFactory {
	fun build(config: SchedulerConfig) =
		DaggerSchedulerFetcherComponent
			.builder()
			.fusedDataComponent(FusedDataComponentFactory.build(config))
			.config(config)
			.build()
			.schedulerFetcher
}
