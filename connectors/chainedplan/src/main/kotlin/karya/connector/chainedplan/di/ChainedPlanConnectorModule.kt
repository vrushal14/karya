package karya.connector.chainedplan.di

import dagger.Binds
import dagger.Module
import karya.connector.chainedplan.ChainedPlanConnector
import karya.core.actors.Connector
import karya.core.entities.Action

@Module
abstract class ChainedPlanConnectorModule {

  @Binds
  @ChainedPlanConnectorScope
  abstract fun provideChainedPlanConnector(connector: ChainedPlanConnector): Connector<Action.ChainedRequest>
}
