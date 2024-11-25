package karya.servers.scheduler.app

import karya.core.configs.KaryaEnvironmentConfig
import karya.servers.scheduler.configs.SchedulerConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.util.concurrent.CountDownLatch

val logger: Logger = LogManager.getLogger()

fun main() {
    val providers = KaryaEnvironmentConfig.PROVIDERS
    val scheduler = KaryaEnvironmentConfig.SCHEDULER
    val config = SchedulerConfig.load(scheduler, providers)

    val latch = CountDownLatch(1)
    val schedulerManager = SchedulerManager(config)

    Runtime.getRuntime().addShutdownHook(
        Thread {
            logger.info("Shutdown hook invoked...")
            schedulerManager.stop()
            println("All worker/fetcher instances stopped. Main thread exiting.")
        },
    )

    schedulerManager.start()
    latch.await()
}
