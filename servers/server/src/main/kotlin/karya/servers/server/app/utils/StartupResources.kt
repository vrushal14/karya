package karya.servers.server.app.utils

import karya.core.repos.RepoConnector
import org.apache.logging.log4j.kotlin.logger
import javax.inject.Inject

class StartupResources
@Inject
constructor(
  private val repoConnector: RepoConnector,
) {
  suspend fun invoke() {
    if (repoConnector.runMigration()) logger.info { "Migration ran successfully..." }
    if (repoConnector.createPartitions()) logger.info { "Partitions created successfully..." }
  }
}
