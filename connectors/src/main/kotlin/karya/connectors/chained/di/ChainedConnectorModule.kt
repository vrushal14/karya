package karya.connectors.chained.di

import dagger.Binds
import dagger.Module
import karya.connectors.chained.ChainedConnector
import karya.core.actors.Connector
import karya.core.entities.Action

@Module
abstract class ChainedConnectorModule {

  @Binds
  @ChainedConnectorScope
  abstract fun provideChainedPlanConnector(connector: ChainedConnector): Connector<Action.ChainedRequest>
}
