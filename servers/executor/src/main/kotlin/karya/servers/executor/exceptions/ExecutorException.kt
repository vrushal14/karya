package karya.servers.executor.exceptions

import karya.core.entities.action.Action
import karya.core.exceptions.KaryaException

sealed class ExecutorException(
  override val message: String
) : KaryaException(message) {

  data class ConnectorNotFoundException(
    val action : Action,
    override val message: String = "Connector not found for action --- $action"
  ) : ExecutorException(message)
}
