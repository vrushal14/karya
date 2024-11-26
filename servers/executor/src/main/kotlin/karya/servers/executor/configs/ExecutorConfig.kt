package karya.servers.executor.configs

import karya.connectors.restapi.configs.RestApiConnectorConfig
import karya.connectors.restapi.di.RestApiConnectorFactory
import karya.core.actors.Connector
import karya.core.configs.QueueConfig
import karya.core.configs.RepoConfig
import karya.core.entities.action.Action
import karya.core.utils.getSection
import karya.data.fused.QueueSelector
import karya.data.fused.RepoSelector

data class ExecutorConfig(
  val connectors: List<Connector<out Action>>,
  val repoConfig: RepoConfig,
  val queueConfig: QueueConfig
) {

  companion object {

    fun load(
      executorFilePath: String,
      providerFilePath: String,
    ): ExecutorConfig {

      val connectors = mutableListOf<Connector<out Action>>()
      val properties: List<*> = getSection(executorFilePath, "connectors")
      connectors.addConnectors(properties)

      return ExecutorConfig(
        connectors = connectors,
        repoConfig = RepoSelector.get(providerFilePath),
        queueConfig = QueueSelector.get(providerFilePath),
      )
    }

    private fun MutableList<Connector<out Action>>.addConnectors(properties: List<*>) {
      for (connector in properties) {
        val connectorMap = connector as Map<*, *>
        if (connectorMap["type"] == RestApiConnectorConfig.REST_API_CONNECTOR_IDENTIFIER) {
          this.add(RestApiConnectorFactory.build(connectorMap["configs"] as Map<*, *>))
        }
      }
    }

  }

}
