package karya.servers.scheduler.di.components

import dagger.BindsInstance
import dagger.Component
import karya.data.fused.di.components.FusedDataLocksComponent
import karya.data.fused.di.components.FusedDataQueueComponent
import karya.data.fused.di.components.FusedDataRepoComponent
import karya.servers.scheduler.configs.SchedulerConfig
import karya.servers.scheduler.di.SchedulerScope
import karya.servers.scheduler.usecases.SchedulerWorker

@SchedulerScope
@Component(
  dependencies = [
    FusedDataRepoComponent::class,
    FusedDataLocksComponent::class,
    FusedDataQueueComponent::class
  ],
)
interface SchedulerWorkerComponent {

  val schedulerWorker: SchedulerWorker

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun executorConfig(schedulerConfig: SchedulerConfig): Builder

    fun fusedRepoComponent(fusedDataRepoComponent: FusedDataRepoComponent): Builder

    fun fusedLocksComponent(fusedDataLocksComponent: FusedDataLocksComponent): Builder

    fun fusedQueueComponent(fusedDataQueueComponent: FusedDataQueueComponent): Builder

    fun build(): SchedulerWorkerComponent
  }
}
