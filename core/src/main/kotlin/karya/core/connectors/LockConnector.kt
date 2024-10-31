package karya.core.connectors

interface LockConnector {
  suspend fun shutdown() : Boolean
}