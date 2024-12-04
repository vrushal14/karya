package karya.servers.executor.exceptions

import karya.core.entities.Action
import karya.core.exceptions.KaryaException

/**
 * Sealed class representing exceptions specific to the executor.
 */
sealed class ExecutorException : KaryaException() {

  /**
   * Exception thrown when a connector is not found for a given action.
   *
   * @property action The action for which the connector was not found.
   * @property message The exception message.
   */
  data class ConnectorNotFoundException(
    val action: Action,
    override val message: String = "Connector not registered for action --- $action"
  ) : ExecutorException()

  /**
   * Exception thrown when an unrecognized connector is passed.
   *
   * @property type The type of the unrecognized connector.
   * @property message The exception message.
   */
  data class UnrecognizedConnectorPassedException(
    val type: String,
    override val message: String = "Unrecognized connector passed --- $type"
  ) : ExecutorException()
}
