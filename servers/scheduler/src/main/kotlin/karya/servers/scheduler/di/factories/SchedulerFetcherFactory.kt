package karya.servers.scheduler.di.factories

import karya.data.fused.di.factories.FusedDataRepoComponentFactory
import karya.servers.scheduler.configs.SchedulerConfig
import karya.servers.scheduler.di.components.DaggerSchedulerFetcherComponent

/**
 * Factory object for creating instances of [SchedulerFetcher].
 */
object SchedulerFetcherFactory {

  /**
   * Builds and returns an instance of [SchedulerFetcher] using the provided configuration.
   *
   * @param config The configuration for the scheduler.
   * @return An instance of [SchedulerFetcher].
   */
  fun build(config: SchedulerConfig) =
    DaggerSchedulerFetcherComponent
      .builder()
      .fusedRepoComponent(FusedDataRepoComponentFactory.build(config.repoConfig))
      .executorConfig(config)
      .build()
      .schedulerFetcher
}
