package karya.servers.scheduler.di.factories

import karya.servers.scheduler.configs.SchedulerConfig
import karya.servers.scheduler.di.components.DaggerSchedulerWorkerComponent

object SchedulerWorkerFactory {
	fun build(schedulerConfig: SchedulerConfig) =
		DaggerSchedulerWorkerComponent
			.builder()
			.fusedDataComponent(FusedDataComponentFactory.build(schedulerConfig))
			.config(schedulerConfig)
			.build()
			.schedulerWorker
}
