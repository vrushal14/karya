package karya.connector.chainedjob.di

import dagger.Binds
import dagger.Module
import karya.connector.chainedjob.ChainedJobConnector
import karya.core.actors.Connector
import karya.core.entities.action.Action

@Module
abstract class ChainedJobConnectorModule {

  @Binds
  @ChainedJobConnectorScope
  abstract fun provideChainedJobConnector(connector: ChainedJobConnector): Connector<Action.ChainedRequest>
}
