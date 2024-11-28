package karya.connector.chainedjob.di

import dagger.Component
import karya.core.actors.Connector
import karya.core.entities.action.Action
import karya.data.fused.di.components.FusedDataRepoComponent

@ChainedJobConnectorScope
@Component(
  dependencies = [
    FusedDataRepoComponent::class
  ],
  modules = [
    ChainedJobConnectorModule::class,
  ],
)
interface ChainedJobConnectorComponent {

  val connector: Connector<Action.ChainedRequest>
}
