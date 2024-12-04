package karya.servers.executor.usecase.internal

import karya.core.actors.Connector
import karya.core.entities.Action
import karya.servers.executor.configs.ExecutorConfig
import karya.servers.executor.exceptions.ExecutorException
import javax.inject.Inject

/**
 * Use case class responsible for retrieving a connector based on the given action.
 *
 * @property config The configuration containing the connectors.
 * @constructor Creates an instance of [GetConnector] with the specified configuration.
 */
class GetConnector
@Inject
constructor(
  private val config: ExecutorConfig
) {

  /**
   * Retrieves the connector associated with the given action.
   *
   * @param action The action for which the connector is to be retrieved.
   * @return The connector associated with the specified action.
   * @throws ExecutorException.ConnectorNotFoundException if no connector is found for the given action.
   */
  fun invoke(action: Action) =
    config.connectors[action::class] as? Connector<Action>
      ?: throw ExecutorException.ConnectorNotFoundException(action)
}
