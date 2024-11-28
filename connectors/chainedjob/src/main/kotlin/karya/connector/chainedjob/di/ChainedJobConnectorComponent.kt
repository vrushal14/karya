package karya.connector.chainedjob.di

import dagger.Component
import karya.core.actors.Connector
import karya.core.entities.action.Action
import karya.data.fused.di.components.FusedRepoDataComponent

@ChainedJobConnectorScope
@Component(
  dependencies = [
    FusedRepoDataComponent::class
  ],
  modules = [
    ChainedJobConnectorModule::class,
  ],
)
interface ChainedJobConnectorComponent {

  val connector: Connector<Action.ChainedRequest>
}
