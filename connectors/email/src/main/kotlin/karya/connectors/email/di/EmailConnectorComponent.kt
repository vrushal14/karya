package karya.connectors.email.di

import dagger.BindsInstance
import dagger.Component
import karya.connectors.email.configs.EmailConnectorConfig
import karya.core.actors.Connector
import karya.core.entities.Action
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    EmailConnectorModule::class
  ]
)
interface EmailConnectorComponent {

  val connector: Connector<Action.EmailRequest>

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun emailConnectorConfig(config: EmailConnectorConfig): Builder

    fun build(): EmailConnectorComponent
  }
}
