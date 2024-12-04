package karya.servers.server.app

import karya.servers.server.app.ktor.KtorServer
import karya.servers.server.app.utils.ShutdownResources
import karya.servers.server.app.utils.StartupResources
import kotlinx.coroutines.runBlocking
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

/**
 * Main application class for the server.
 *
 * @property ktorServer The Ktor server instance.
 * @property startupResources The resources needed for startup.
 * @property shutdownResources The resources needed for shutdown.
 */
class ServerApplication
@Inject
constructor(
  private val ktorServer: KtorServer,
  private val startupResources: StartupResources,
  private val shutdownResources: ShutdownResources,
) {
  companion object : Logging

  /**
   * Starts the server application.
   */
  fun start() {
    runBlocking {
      startupResources.invoke()
      ktorServer.start()
      logger.info { "Server started successfully" }
    }
  }

  /**
   * Stops the server application.
   */
  fun stop() {
    runBlocking {
      shutdownResources.invoke()
      ktorServer.stop()
      logger.info { "Server shutdown complete" }
    }
  }
}
