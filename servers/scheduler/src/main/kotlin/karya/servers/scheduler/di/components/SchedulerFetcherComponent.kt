package karya.servers.scheduler.di.components

import dagger.BindsInstance
import dagger.Component
import karya.data.fused.di.FusedDataComponent
import karya.servers.scheduler.configs.SchedulerConfig
import karya.servers.scheduler.di.SchedulerScope
import karya.servers.scheduler.usecases.SchedulerFetcher

@SchedulerScope
@Component(
  dependencies = [
    FusedDataComponent::class
  ]
)
interface SchedulerFetcherComponent {

  val schedulerFetcher : SchedulerFetcher

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun config(schedulerConfig: SchedulerConfig) : Builder

    fun fusedDataComponent(fusedDataComponent: FusedDataComponent): Builder

    fun build() : SchedulerFetcherComponent
  }
}