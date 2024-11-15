package karya.servers.scheduler.di.components

import dagger.BindsInstance
import dagger.Component
import karya.data.fused.di.FusedDataComponent
import karya.servers.scheduler.usecases.SchedulerWorker
import karya.servers.scheduler.configs.SchedulerConfig
import karya.servers.scheduler.di.SchedulerScope

@SchedulerScope
@Component(
  dependencies = [
    FusedDataComponent::class
  ]
)
interface SchedulerWorkerComponent {

  val schedulerWorker : SchedulerWorker

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun config(schedulerConfig: SchedulerConfig) : Builder

    fun fusedDataComponent(fusedDataComponent: FusedDataComponent): Builder

    fun build() : SchedulerWorkerComponent
  }
}