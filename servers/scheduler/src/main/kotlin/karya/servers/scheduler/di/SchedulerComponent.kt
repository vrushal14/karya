package karya.servers.scheduler.di

import dagger.BindsInstance
import dagger.Component
import karya.data.fused.di.FusedDataComponent
import karya.servers.scheduler.configs.SchedulerConfig
import karya.servers.scheduler.usecases.external.GetOpenTask
import karya.servers.scheduler.usecases.external.ProcessTask

@SchedulerScope
@Component(
  dependencies = [
    FusedDataComponent::class
  ]
)
interface SchedulerComponent {

  val getOpenTask : GetOpenTask
  val processTask : ProcessTask

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun config(schedulerConfig: SchedulerConfig) : Builder

    fun fusedDataComponent(fusedDataComponent: FusedDataComponent): Builder

    fun build() : SchedulerComponent
  }
}