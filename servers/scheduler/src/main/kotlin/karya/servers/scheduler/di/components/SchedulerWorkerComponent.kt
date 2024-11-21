package karya.servers.scheduler.di.components

import dagger.BindsInstance
import dagger.Component
import karya.data.fused.di.components.FusedSchedulerDataComponent
import karya.servers.scheduler.configs.SchedulerConfig
import karya.servers.scheduler.di.SchedulerScope
import karya.servers.scheduler.usecases.SchedulerWorker

@SchedulerScope
@Component(
	dependencies = [
		FusedSchedulerDataComponent::class,
	],
)
interface SchedulerWorkerComponent {
	val schedulerWorker: SchedulerWorker

	@Component.Builder
	interface Builder {
		@BindsInstance
		fun config(schedulerConfig: SchedulerConfig): Builder

		fun fusedDataComponent(fusedSchedulerDataComponent: FusedSchedulerDataComponent): Builder

		fun build(): SchedulerWorkerComponent
	}
}
