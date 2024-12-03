package karya.connector.chainedplan.di

import dagger.Component
import karya.core.actors.Connector
import karya.core.entities.Action
import karya.data.fused.di.components.FusedDataRepoComponent

@ChainedPlanConnectorScope
@Component(
  dependencies = [
    FusedDataRepoComponent::class
  ],
  modules = [
    ChainedPlanConnectorModule::class,
  ],
)
interface ChainedPlanConnectorComponent {

  val connector: Connector<Action.ChainedRequest>
}
