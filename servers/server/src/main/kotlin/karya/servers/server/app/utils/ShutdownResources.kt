package karya.servers.server.app.utils

import karya.core.locks.LocksClient
import karya.core.repos.RepoConnector
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

/**
 * Class responsible for handling shutdown resources.
 *
 * @property repoConnector The repository connector used for database operations.
 * @property locksClient The locks client used for managing locks.
 */
class ShutdownResources
@Inject
constructor(
  private val repoConnector: RepoConnector,
  private val locksClient: LocksClient,
) {
  companion object : Logging

  /**
   * Invokes the shutdown resources operations.
   *
   * Shuts down the repository connector and locks client, and logs the shutdown status.
   */
  suspend fun invoke() {
    if (repoConnector.shutdown()) logger.info { "Repo connector shutdown successfully..." }
    if (locksClient.shutdown()) logger.info { "Locks connector shutdown successfully..." }
    logger.info { "Shutdown Resources complete" }
  }
}
