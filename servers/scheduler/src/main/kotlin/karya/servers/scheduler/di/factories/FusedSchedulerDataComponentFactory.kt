package karya.servers.scheduler.di.factories

import karya.data.fused.di.components.DaggerFusedSchedulerDataComponent
import karya.servers.scheduler.configs.SchedulerConfig

object FusedSchedulerDataComponentFactory {
  fun build(schedulerConfig: SchedulerConfig) =
    DaggerFusedSchedulerDataComponent
      .builder()
      .repoConfig(schedulerConfig.repoConfig)
      .locksConfig(schedulerConfig.locksConfig)
      .queueConfig(schedulerConfig.queueConfig)
      .build()
}
