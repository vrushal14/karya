package karya.servers.server.app.utils

import karya.core.connectors.RepoConnector
import karya.core.connectors.LockConnector
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class ShutdownResources
@Inject
constructor(
  private val repoConnector: RepoConnector,
  private val lockConnector: LockConnector
) {

  companion object : Logging

  suspend fun invoke() {
    if(repoConnector.shutdown()) logger.info { "Repo connector shutdown successfully..." }
    if(lockConnector.shutdown()) logger.info { "Locks connector shutdown successfully..." }
    logger.info("Repo Connector shutdown complete")
  }
}