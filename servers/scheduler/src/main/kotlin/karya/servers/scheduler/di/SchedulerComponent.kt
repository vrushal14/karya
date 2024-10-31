package karya.servers.scheduler.di

import dagger.BindsInstance
import dagger.Component
import karya.data.fused.di.FusedDataComponent
import karya.servers.scheduler.app.SchedulerApplication
import karya.servers.scheduler.configs.SchedulerConfig

@SchedulerScope
@Component(
  dependencies = [
    FusedDataComponent::class
  ]
)
interface SchedulerComponent {

  val schedulerApplication : SchedulerApplication

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun config(schedulerConfig: SchedulerConfig) : Builder

    fun fusedDataComponent(fusedDataComponent: FusedDataComponent): Builder

    fun build() : SchedulerComponent
  }
}