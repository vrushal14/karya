package karya.servers.server.di

import dagger.Component
import karya.data.fused.di.FusedDataComponent
import karya.servers.server.app.ServerApplication

@ServerScope
@Component(
  dependencies = [
    FusedDataComponent::class
  ]
)
interface ServerComponent {
  val serverApplication : ServerApplication
}