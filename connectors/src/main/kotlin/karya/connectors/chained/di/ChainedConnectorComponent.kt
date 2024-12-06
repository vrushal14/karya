package karya.connectors.chained.di

import dagger.Component
import karya.core.actors.Connector
import karya.core.entities.Action
import karya.data.fused.di.components.FusedDataRepoComponent

@ChainedConnectorScope
@Component(
  dependencies = [
    FusedDataRepoComponent::class
  ],
  modules = [
    ChainedConnectorModule::class,
  ],
)
interface ChainedConnectorComponent {

  val connector: Connector<Action.ChainedRequest>
}
