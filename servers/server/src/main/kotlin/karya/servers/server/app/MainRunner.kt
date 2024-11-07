package karya.servers.server.app

import karya.core.utils.KaryaEnvironmentConfig
import karya.data.fused.repos.RepoConfig
import karya.data.fused.locks.LocksConfig
import karya.servers.server.di.ServerApplicationFactory
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CountDownLatch

suspend fun main() {

  val providers = KaryaEnvironmentConfig.PROVIDERS
  val repoConfig = RepoConfig.fromYaml(providers)
  val locksConfig = LocksConfig.fromYaml(providers)

  val serverApplication = ServerApplicationFactory.create(repoConfig, locksConfig)
  val latch = CountDownLatch(1)

  Runtime.getRuntime().addShutdownHook(Thread {
    runBlocking {
        serverApplication.stop()
        latch.countDown()
      }
  })

  serverApplication.start()
  latch.await()
}