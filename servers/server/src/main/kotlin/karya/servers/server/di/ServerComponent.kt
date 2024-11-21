package karya.servers.server.di

import dagger.Component
import karya.data.fused.di.components.FusedServerDataComponent
import karya.servers.server.app.ServerApplication

@ServerScope
@Component(
	dependencies = [
		FusedServerDataComponent::class,
	],
)
interface ServerComponent {
	val serverApplication: ServerApplication
}
