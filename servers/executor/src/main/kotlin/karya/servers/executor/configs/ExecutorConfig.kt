package karya.servers.executor.configs

import karya.connector.chainedplan.di.ChainedPlanConnectorFactory
import karya.connectors.restapi.di.RestApiConnectorFactory
import karya.connectors.slackmessage.di.SlackMessageConnectorFactory
import karya.core.actors.Connector
import karya.core.entities.Action
import karya.core.entities.Action.*
import karya.core.utils.getSection
import karya.data.fused.QueueSelector
import karya.data.fused.RepoSelector
import karya.data.fused.di.components.FusedDataQueueComponent
import karya.data.fused.di.components.FusedDataRepoComponent
import karya.data.fused.di.factories.FusedDataQueueComponentFactory
import karya.data.fused.di.factories.FusedDataRepoComponentFactory
import karya.servers.executor.exceptions.ExecutorException
import kotlin.reflect.KClass
import karya.connectors.restapi.configs.RestApiConnectorConfig as RestApi
import karya.connectors.slackmessage.configs.SlackMessageConnectorConfig as Slack

data class ExecutorConfig(
  val fusedDataRepoComponent: FusedDataRepoComponent,
  val fusedDataQueueComponent: FusedDataQueueComponent
) {

  lateinit var connectors: MutableMap<KClass<out Action>, Connector<out Action>>

  companion object {

    fun load(executorFilePath: String, providerFilePath: String): ExecutorConfig {

      val application: Map<String, *> = getSection(executorFilePath, "application")
      val repoConfig = RepoSelector.get(providerFilePath)
      val queueConfig = QueueSelector.get(providerFilePath)

      return ExecutorConfig(
        fusedDataRepoComponent = FusedDataRepoComponentFactory.build(repoConfig),
        fusedDataQueueComponent = FusedDataQueueComponentFactory.build(queueConfig)
      )
        .addDefaultConnectors()
        .addCustomConnectors(application["connectors"] as List<*>)
    }

    private fun ExecutorConfig.addDefaultConnectors(): ExecutorConfig {
      this.connectors = mutableMapOf()
      this.connectors[ChainedRequest::class] = ChainedPlanConnectorFactory.build(this.fusedDataRepoComponent)
      return this
    }

    private fun ExecutorConfig.addCustomConnectors(properties: List<*>): ExecutorConfig {
      properties.forEach { connector ->
        val connectorMap = connector as? Map<*, *> ?: return@forEach
        val type = connectorMap["type"] as? String ?: return@forEach
        val configs = connectorMap["configs"] as? Map<*, *> ?: return@forEach

        when (type) {
          RestApi.IDENTIFIER -> this.connectors[RestApiRequest::class] = RestApiConnectorFactory.build(configs)
          Slack.IDENTIFIER -> this.connectors[SlackMessageRequest::class] = SlackMessageConnectorFactory.build(configs)

          else -> throw ExecutorException.UnrecognizedConnectorPassedException(type)
        }
      }
      return this
    }

  }
}
