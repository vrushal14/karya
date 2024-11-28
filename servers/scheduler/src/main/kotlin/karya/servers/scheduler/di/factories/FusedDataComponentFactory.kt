package karya.servers.scheduler.di.factories

import karya.data.fused.di.factories.FusedSchedulerDataComponentFactory
import karya.servers.scheduler.configs.SchedulerConfig

object FusedDataComponentFactory {
  fun build(config: SchedulerConfig) =
    FusedSchedulerDataComponentFactory.build(
      repoConfig = config.repoConfig,
      queueConfig = config.queueConfig,
      locksConfig = config.locksConfig
    )
}
