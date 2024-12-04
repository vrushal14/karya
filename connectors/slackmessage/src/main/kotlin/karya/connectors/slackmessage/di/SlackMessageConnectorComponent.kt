package karya.connectors.slackmessage.di

import dagger.BindsInstance
import dagger.Component
import karya.connectors.slackmessage.configs.SlackMessageConnectorConfig
import karya.core.actors.Connector
import karya.core.entities.Action
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    SlackMessageConnectorModule::class,
  ],
)
interface SlackMessageConnectorComponent {

  val connector: Connector<Action.SlackMessageRequest>

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun config(config: SlackMessageConnectorConfig): Builder

    fun build(): SlackMessageConnectorComponent
  }
}
