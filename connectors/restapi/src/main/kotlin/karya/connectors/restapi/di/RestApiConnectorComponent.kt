package karya.connectors.restapi.di

import dagger.BindsInstance
import dagger.Component
import karya.connectors.restapi.configs.RestApiConnectorConfig
import karya.core.actors.Connector
import karya.core.entities.Action
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    RestApiConnectorModule::class,
  ],
)
interface RestApiConnectorComponent {

  val connector: Connector<Action.RestApiRequest>

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun config(config: RestApiConnectorConfig): Builder

    fun build(): RestApiConnectorComponent
  }
}
