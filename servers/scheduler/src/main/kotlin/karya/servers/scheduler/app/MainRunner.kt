package karya.servers.scheduler.app

import karya.core.utils.getConfigPath
import karya.data.fused.locks.LocksConfig
import karya.data.fused.repos.RepoConfig
import karya.servers.scheduler.configs.SchedulerConfig
import karya.servers.scheduler.di.SchedulerComponentFactory
import java.util.concurrent.CountDownLatch

suspend fun main() {

  val valuesFile = getConfigPath("servers/scheduler")
  val repoConfig = RepoConfig.fromYaml(valuesFile)
  val locksConfig = LocksConfig.fromYaml(valuesFile)
  val schedulerConfig = SchedulerConfig(
    pollFrequency = 500L,
    partitions = listOf(1,2,3,4,5),
    executionBufferInMilli = 500L
  )

  val component = SchedulerComponentFactory.create(repoConfig, locksConfig, schedulerConfig)

  val pollerService = PollerService(
    config = schedulerConfig,
    ioCall = component.getOpenTask::invoke,
    onResult = { task -> component.processTask.invoke(task) }
  )
  val latch = CountDownLatch(1)

  Runtime.getRuntime().addShutdownHook(Thread {
    pollerService.stop()
    latch.countDown()
  })

  pollerService.start()
  latch.await()

}