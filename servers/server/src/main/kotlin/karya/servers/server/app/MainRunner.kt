package karya.servers.server.app

import karya.core.configs.KaryaEnvironmentConfig
import karya.servers.server.configs.ServerConfig
import karya.servers.server.di.ServerApplicationFactory
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CountDownLatch

/**
 * Main entry point for the server application.
 */
fun main() {
  val providers = KaryaEnvironmentConfig.PROVIDERS
  val server = KaryaEnvironmentConfig.SERVER

  // Load the server configuration
  val config = ServerConfig.load(providers, server)

  // Create the server application
  val serverApplication = ServerApplicationFactory.create(config)
  val latch = CountDownLatch(1)

  // Add a shutdown hook to stop the server gracefully
  Runtime.getRuntime().addShutdownHook(
    Thread {
      runBlocking {
        serverApplication.stop()
        latch.countDown()
      }
    },
  )

  // Start the server application and wait for the shutdown signal
  serverApplication.start()
  latch.await()
}
