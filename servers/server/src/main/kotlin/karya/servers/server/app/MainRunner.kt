package karya.servers.server.app

import karya.core.configs.KaryaEnvironmentConfig
import karya.data.fused.LocksSelector
import karya.data.fused.RepoSelector
import karya.servers.server.di.ServerApplicationFactory
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CountDownLatch

suspend fun main() {
    val providers = KaryaEnvironmentConfig.PROVIDERS
    val repoConfig = RepoSelector.get(providers)
    val locksConfig = LocksSelector.get(providers)

    val serverApplication = ServerApplicationFactory.create(repoConfig, locksConfig)
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
