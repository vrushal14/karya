package karya.connectors.slackmessage.di

import com.slack.api.Slack
import com.slack.api.methods.MethodsClient
import dagger.Module
import dagger.Provides
import karya.connectors.slackmessage.SlackMessageConnector
import karya.connectors.slackmessage.configs.SlackMessageConnectorConfig
import karya.core.actors.Connector
import karya.core.entities.Action
import javax.inject.Singleton

@Module
class SlackMessageConnectorModule {

  @Provides
  @Singleton
  fun provideSlackMessageConnector(methodsClient: MethodsClient, slack: Slack): Connector<Action.SlackMessageRequest> =
    SlackMessageConnector(methodsClient, slack)

  @Provides
  @Singleton
  fun provideMethodsClient(slack: Slack, config: SlackMessageConnectorConfig): MethodsClient =
    slack.methods(config.token)

  @Provides
  @Singleton
  fun provideSlack(): Slack =
    Slack.getInstance()
}
