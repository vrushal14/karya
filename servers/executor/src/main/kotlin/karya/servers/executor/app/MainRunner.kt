package karya.servers.executor.app

import karya.core.configs.KaryaEnvironmentConfig
import karya.servers.executor.configs.ExecutorConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.util.concurrent.CountDownLatch

val logger: Logger = LogManager.getLogger()

suspend fun main() {
  val providers = KaryaEnvironmentConfig.PROVIDERS
  val executor = KaryaEnvironmentConfig.EXECUTOR
  val config = ExecutorConfig.load(executor, providers)

  val latch = CountDownLatch(1)

  Runtime.getRuntime().addShutdownHook(
    Thread {
      logger.info("Shutdown hook invoked...")
      println("All executor instances stopped. Main thread exiting.")
    }
  )

  latch.await()
}
