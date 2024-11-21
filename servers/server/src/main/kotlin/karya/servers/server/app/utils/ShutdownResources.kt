package karya.servers.server.app.utils

import karya.core.locks.LocksClient
import karya.core.repos.RepoConnector
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class ShutdownResources
	@Inject
	constructor(
		private val repoConnector: RepoConnector,
		private val locksClient: LocksClient,
	) {
		companion object : Logging

		suspend fun invoke() {
			if (repoConnector.shutdown()) logger.info { "Repo connector shutdown successfully..." }
			if (locksClient.shutdown()) logger.info { "Locks connector shutdown successfully..." }
			logger.info("Repo Connector shutdown complete")
		}
	}
