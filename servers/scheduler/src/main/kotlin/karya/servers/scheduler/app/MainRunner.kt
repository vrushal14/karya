package karya.servers.scheduler.app

import karya.core.utils.KaryaEnvironmentConfig
import karya.servers.scheduler.configs.SchedulerConfig
import karya.servers.scheduler.di.SchedulerApplicationFactory
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

val logger: Logger = LogManager.getLogger()

fun runApplicationInstance(instanceId: Int, latch: CountDownLatch, schedulerConfig: SchedulerConfig) {
  logger.info("Starting application instance $instanceId")
  val application = SchedulerApplicationFactory.create(schedulerConfig)

  Runtime.getRuntime().addShutdownHook(Thread {
    logger.info("Shutting down application instance $instanceId")
    application.stop()
    latch.countDown()
  })

  try {
    application.start(instanceId)
    Thread.currentThread().join() // Keep this thread alive until the application is stopped via the shutdown hook.

  } catch (e: InterruptedException) {
    logger.warn("Application instance $instanceId interrupted: ${e.message}")
    Thread.currentThread().interrupt() // Preserve interrupt status

  } finally {
    println("Application instance $instanceId has stopped.")
  }
}

fun main() {
  val providers = KaryaEnvironmentConfig.PROVIDERS
  val scheduler = KaryaEnvironmentConfig.SCHEDULER
  val schedulerConfig = SchedulerConfig.load(scheduler, providers)

  val executorService = Executors.newFixedThreadPool(schedulerConfig.workers)
  val latch = CountDownLatch(schedulerConfig.workers)

  try {
    repeat(schedulerConfig.workers) { instanceId ->
      logger.info("Instance $instanceId will start after ${schedulerConfig.startDelay} ms")
      Thread.sleep(schedulerConfig.startDelay)

      executorService.submit {
        try {
          runApplicationInstance(instanceId, latch, schedulerConfig)

        } catch (e: Exception) {
          logger.error("Instance $instanceId encountered an error: ${e.message}")
          latch.countDown() // Ensure latch decrements even on failure
        }
      }
    }

    Runtime.getRuntime().addShutdownHook(Thread {
      logger.info("Shutdown initiated.")
      executorService.shutdownNow()
    })

    latch.await() // Wait until all workers signal completion
  } catch (e: InterruptedException) {
    logger.warn("Main thread interrupted: ${e.message}")
    Thread.currentThread().interrupt() // Preserve interrupt status

  } finally {
    executorService.shutdown()
    println("All instances stopped. Main thread exiting.")
  }
}
