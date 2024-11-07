package karya.servers.scheduler.app

import karya.core.utils.KaryaEnvironmentConfig
import karya.data.fused.locks.LocksConfig
import karya.data.fused.repos.RepoConfig
import karya.servers.scheduler.configs.SchedulerConfig
import karya.servers.scheduler.di.SchedulerApplicationFactory
import java.util.concurrent.CountDownLatch

fun main() {

  val providers = KaryaEnvironmentConfig.PROVIDERS
  val scheduler = KaryaEnvironmentConfig.SCHEDULER

  val repoConfig = RepoConfig.fromYaml(providers)
  val locksConfig = LocksConfig.fromYaml(providers)
  val schedulerConfig = SchedulerConfig.load(scheduler)

  val application = SchedulerApplicationFactory.create(repoConfig, locksConfig, schedulerConfig)
  val latch = CountDownLatch(1)

  Runtime.getRuntime().addShutdownHook(Thread {
    application.stop()
    latch.countDown()
  })

  application.start()
  latch.await()

}