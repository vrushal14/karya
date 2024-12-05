package karya.connectors.slack.di

import dagger.BindsInstance
import dagger.Component
import karya.connectors.slack.configs.SlackConnectorConfig
import karya.core.actors.Connector
import karya.core.entities.Action
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    SlackConnectorModule::class,
  ],
)
interface SlackConnectorComponent {

  val connector: Connector<Action.SlackMessageRequest>

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun slackMessageConnectorConfig(config: SlackConnectorConfig): Builder

    fun build(): SlackConnectorComponent
  }
}
