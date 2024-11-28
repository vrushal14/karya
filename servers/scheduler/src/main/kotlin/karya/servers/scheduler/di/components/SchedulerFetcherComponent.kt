package karya.servers.scheduler.di.components

import dagger.BindsInstance
import dagger.Component
import karya.data.fused.di.components.FusedDataRepoComponent
import karya.servers.scheduler.configs.SchedulerConfig
import karya.servers.scheduler.di.SchedulerScope
import karya.servers.scheduler.usecases.SchedulerFetcher

@SchedulerScope
@Component(
  dependencies = [
    FusedDataRepoComponent::class
  ],
)
interface SchedulerFetcherComponent {

  val schedulerFetcher: SchedulerFetcher

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun executorConfig(schedulerConfig: SchedulerConfig): Builder

    fun fusedRepoComponent(fusedDataRepoComponent: FusedDataRepoComponent): Builder

    fun build(): SchedulerFetcherComponent
  }
}
