package karya.connectors.slack.di

import com.slack.api.Slack
import com.slack.api.methods.MethodsClient
import dagger.Module
import dagger.Provides
import karya.connectors.slack.SlackConnector
import karya.connectors.slack.configs.SlackConnectorConfig
import karya.core.actors.Connector
import karya.core.entities.Action
import javax.inject.Singleton

@Module
class SlackConnectorModule {

  @Provides
  @Singleton
  fun provideSlackMessageConnector(methodsClient: MethodsClient, slack: Slack): Connector<Action.SlackMessageRequest> =
    SlackConnector(methodsClient, slack)

  @Provides
  @Singleton
  fun provideMethodsClient(slack: Slack, config: SlackConnectorConfig): MethodsClient =
    slack.methods(config.token)

  @Provides
  @Singleton
  fun provideSlack(): Slack =
    Slack.getInstance()
}
