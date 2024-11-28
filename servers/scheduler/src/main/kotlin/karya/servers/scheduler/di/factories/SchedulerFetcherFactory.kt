package karya.servers.scheduler.di.factories

import karya.data.fused.di.factories.FusedDataRepoComponentFactory
import karya.servers.scheduler.configs.SchedulerConfig
import karya.servers.scheduler.di.components.DaggerSchedulerFetcherComponent

object SchedulerFetcherFactory {
  fun build(config: SchedulerConfig) =
    DaggerSchedulerFetcherComponent
      .builder()
      .fusedRepoComponent(FusedDataRepoComponentFactory.build(config.repoConfig))
      .executorConfig(config)
      .build()
      .schedulerFetcher
}
