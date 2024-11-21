package karya.servers.server.app

import karya.servers.server.app.ktor.KtorServer
import karya.servers.server.app.utils.ShutdownResources
import karya.servers.server.app.utils.StartupResources
import kotlinx.coroutines.runBlocking
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class ServerApplication
	@Inject
	constructor(
		private val ktorServer: KtorServer,
		private val startupResources: StartupResources,
		private val shutdownResources: ShutdownResources,
	) {
		companion object : Logging

		suspend fun start() {
			runBlocking {
				startupResources.invoke()
				ktorServer.start()
				logger.info("Server started successfully")
			}
		}

		fun stop() {
			runBlocking {
				shutdownResources.invoke()
				ktorServer.stop()
				logger.info("Server shutdown complete")
				LogManager.shutdown()
			}
		}
	}
