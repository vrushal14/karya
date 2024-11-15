package karya.servers.scheduler.di.factories

import karya.data.fused.di.DaggerFusedDataComponent
import karya.servers.scheduler.configs.SchedulerConfig

object FusedRepoComponentFactory {

  fun create(schedulerConfig: SchedulerConfig) =
    DaggerFusedDataComponent.builder()
      .repoConfig(schedulerConfig.repoConfig)
      .locksConfig(schedulerConfig.locksConfig)
      .build()
}