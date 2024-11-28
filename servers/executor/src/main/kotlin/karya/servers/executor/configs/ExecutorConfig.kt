package karya.servers.executor.configs

import karya.connector.chainedjob.di.ChainedJobConnectorFactory
import karya.connectors.restapi.configs.RestApiConnectorConfig
import karya.connectors.restapi.di.RestApiConnectorFactory
import karya.core.actors.Connector
import karya.core.entities.action.Action
import karya.core.entities.action.Action.ChainedRequest
import karya.core.entities.action.Action.RestApiRequest
import karya.core.utils.getSection
import karya.data.fused.QueueSelector
import karya.data.fused.RepoSelector
import karya.data.fused.di.components.FusedDataQueueComponent
import karya.data.fused.di.components.FusedDataRepoComponent
import karya.data.fused.di.factories.FusedDataQueueComponentFactory
import karya.data.fused.di.factories.FusedDataRepoComponentFactory
import kotlin.reflect.KClass

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
      this.connectors[ChainedRequest::class] = ChainedJobConnectorFactory.build(this.fusedDataRepoComponent)
      return this
    }

    private fun ExecutorConfig.addCustomConnectors(properties: List<*>): ExecutorConfig {
      for (connector in properties) {
        val connectorMap = connector as Map<*, *>
        if (connectorMap["type"] == RestApiConnectorConfig.IDENTIFIER) {
          this.connectors[RestApiRequest::class] = RestApiConnectorFactory.build(connectorMap["configs"] as Map<*, *>)
        }
      }
      return this
    }
  }
}
