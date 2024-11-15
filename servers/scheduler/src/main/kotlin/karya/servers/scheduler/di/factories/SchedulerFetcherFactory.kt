package karya.servers.scheduler.di.factories

import karya.servers.scheduler.configs.SchedulerConfig
import karya.servers.scheduler.di.components.DaggerSchedulerFetcherComponent

object SchedulerFetcherFactory {

  fun create(config : SchedulerConfig) =
    DaggerSchedulerFetcherComponent.builder()
      .fusedDataComponent(FusedRepoComponentFactory.create(config))
      .config(config)
      .build()
      .schedulerFetcher
}