package karya.servers.executor.exceptions

import karya.core.entities.Action
import karya.core.exceptions.KaryaException

sealed class ExecutorException : KaryaException() {

  data class ConnectorNotFoundException(
    val action: Action,
    override val message: String = "Connector not registered for action --- $action"
  ) : ExecutorException()
}
