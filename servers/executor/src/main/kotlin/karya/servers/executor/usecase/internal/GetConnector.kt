package karya.servers.executor.usecase.internal

import karya.core.actors.Connector
import karya.core.entities.Action
import karya.servers.executor.configs.ExecutorConfig
import karya.servers.executor.exceptions.ExecutorException
import javax.inject.Inject

class GetConnector
@Inject
constructor(
  private val config: ExecutorConfig
) {

  fun invoke(action: Action) =
    config.connectors[action::class] as? Connector<Action>
      ?: throw ExecutorException.ConnectorNotFoundException(action)
}
