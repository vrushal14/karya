package karya.servers.executor.configs

import karya.connectors.restapi.configs.RestApiConnectorConfig
import karya.connectors.restapi.di.RestApiConnectorFactory
import karya.core.actors.Connector
import karya.core.configs.QueueConfig
import karya.core.configs.RepoConfig
import karya.core.entities.action.Action
import karya.core.utils.getSection
import karya.core.utils.readValue
import karya.data.fused.QueueSelector
import karya.data.fused.RepoSelector
import kotlin.reflect.KClass

data class ExecutorConfig(
  val workers: Int,
  val connectors: Map<KClass<out Action>, Connector<out Action>>,

  val repoConfig: RepoConfig,
  val queueConfig: QueueConfig
) {

  companion object {

    fun load(
      executorFilePath: String,
      providerFilePath: String,
    ): ExecutorConfig {

      val application: Map<String, *> = getSection(executorFilePath, "application")

      val connectors = mutableMapOf<KClass<out Action>, Connector<out Action>>()
      connectors.addConnectors(application["connectors"] as List<*>)

      return ExecutorConfig(
        workers = application.readValue("workers"),
        connectors = connectors,

        repoConfig = RepoSelector.get(providerFilePath),
        queueConfig = QueueSelector.get(providerFilePath),
      )
    }

    private fun MutableMap<KClass<out Action>, Connector<out Action>>.addConnectors(properties: List<*>) {
      for (connector in properties) {
        val connectorMap = connector as Map<*, *>
        if (connectorMap["type"] == RestApiConnectorConfig.IDENTIFIER) {
          this[Action.RestApiRequest::class] = RestApiConnectorFactory.build(connectorMap["configs"] as Map<*, *>)
        }
      }
    }

  }

}
