package karya.servers.scheduler.di.factories

import karya.servers.scheduler.configs.SchedulerConfig
import karya.servers.scheduler.di.components.DaggerSchedulerWorkerComponent

object SchedulerWorkerFactory {

  fun create(schedulerConfig: SchedulerConfig) =
    DaggerSchedulerWorkerComponent.builder()
      .fusedDataComponent(FusedRepoComponentFactory.create(schedulerConfig))
      .config(schedulerConfig)
      .build()
      .schedulerWorker

}