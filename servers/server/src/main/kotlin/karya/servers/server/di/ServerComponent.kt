package karya.servers.server.di

import dagger.Component
import karya.data.fused.di.components.FusedDataLocksComponent
import karya.data.fused.di.components.FusedDataRepoComponent
import karya.servers.server.app.ServerApplication

@ServerScope
@Component(
  dependencies = [
    FusedDataRepoComponent::class,
    FusedDataLocksComponent::class
  ],
)
interface ServerComponent {
  val serverApplication: ServerApplication
}
