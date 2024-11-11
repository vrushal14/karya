package karya.servers.scheduler.app

import karya.core.locks.LocksClient
import karya.core.repos.RepoConnector
import karya.servers.scheduler.usecases.PollerService
import kotlinx.coroutines.runBlocking
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class SchedulerApplication
@Inject
constructor(
  private val pollerService: PollerService,
  private val repoConnector: RepoConnector,
  private val locksClient : LocksClient
) {

  companion object : Logging

  fun start(instanceId : Int) {
    logger.info { "Starting poller" }
    pollerService.start(setWorkerName(instanceId))
  }

  fun stop() = runBlocking {
    logger.info { "Shutting down poller" }
    pollerService.stop()
    repoConnector.shutdown()
    locksClient.shutdown()
  }

  private fun setWorkerName(instanceId: Int): String {
    val name = "scheduler-karya-worker-$instanceId"
    Thread.currentThread().name = name
    return name
  }

}