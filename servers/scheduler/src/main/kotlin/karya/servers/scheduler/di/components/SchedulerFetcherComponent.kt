package karya.servers.scheduler.di.components

import dagger.BindsInstance
import dagger.Component
import karya.data.fused.di.components.FusedSchedulerDataComponent
import karya.servers.scheduler.configs.SchedulerConfig
import karya.servers.scheduler.di.SchedulerScope
import karya.servers.scheduler.usecases.SchedulerFetcher

@SchedulerScope
@Component(
  dependencies = [
    FusedSchedulerDataComponent::class
  ]
)
interface SchedulerFetcherComponent {

  val schedulerFetcher : SchedulerFetcher

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun config(schedulerConfig: SchedulerConfig) : Builder

    fun fusedDataComponent(fusedSchedulerDataComponent: FusedSchedulerDataComponent): Builder

    fun build() : SchedulerFetcherComponent
  }
}