package karya.servers.server.app

import karya.core.configs.KaryaEnvironmentConfig
import karya.servers.server.configs.ServerConfig
import karya.servers.server.di.ServerApplicationFactory
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CountDownLatch

fun main() {
  val providers = KaryaEnvironmentConfig.PROVIDERS
  val server = KaryaEnvironmentConfig.SERVER

  val config = ServerConfig.load(providers, server)

  val serverApplication = ServerApplicationFactory.create(config)
  val latch = CountDownLatch(1)

  Runtime.getRuntime().addShutdownHook(
    Thread {
      runBlocking {
        serverApplication.stop()
        latch.countDown()
      }
    },
  )

  serverApplication.start()
  latch.await()
}
