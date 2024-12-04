package karya.servers.server.app.utils

import karya.core.repos.RepoConnector
import org.apache.logging.log4j.kotlin.logger
import javax.inject.Inject

/**
 * Class responsible for handling startup resources.
 *
 * @property repoConnector The repository connector used for database operations.
 */
class StartupResources
@Inject
constructor(
  private val repoConnector: RepoConnector,
) {

  /**
   * Invokes the startup resources operations.
   *
   * Runs database migration and creates partitions if necessary.
   */
  suspend fun invoke() {
    if (repoConnector.runMigration()) logger.info { "Migration ran successfully..." }
    if (repoConnector.createPartitions()) logger.info { "Partitions created successfully..." }
  }
}
