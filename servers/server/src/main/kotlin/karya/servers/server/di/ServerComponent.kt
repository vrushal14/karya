package karya.servers.server.di

import dagger.BindsInstance
import dagger.Component
import karya.data.fused.di.components.FusedDataLocksComponent
import karya.data.fused.di.components.FusedDataRepoComponent
import karya.servers.server.app.ServerApplication
import karya.servers.server.configs.ServerConfig

@ServerScope
@Component(
  dependencies = [
    FusedDataRepoComponent::class,
    FusedDataLocksComponent::class
  ],
)
interface ServerComponent {
  val serverApplication: ServerApplication

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun serverConfig(config: ServerConfig): Builder

    fun fusedDataRepoComponent(fusedDataRepoComponent: FusedDataRepoComponent): Builder

    fun fusedDataLocksComponent(fusedDataLocksComponent: FusedDataLocksComponent): Builder

    fun build(): ServerComponent
  }
}
