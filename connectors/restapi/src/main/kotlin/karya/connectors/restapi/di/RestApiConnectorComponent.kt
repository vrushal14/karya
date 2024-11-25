package karya.connectors.restapi.di

import dagger.BindsInstance
import dagger.Component
import karya.connectors.restapi.configs.RestApiConnectorConfig
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    RestApiConnectorModule::class,
  ],
)
interface RestApiConnectorComponent {
  @Component.Builder
  interface Builder {
    @BindsInstance
    fun config(config: RestApiConnectorConfig): Builder

    fun build(): RestApiConnectorComponent
  }
}
