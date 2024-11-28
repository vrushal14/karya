package karya.servers.scheduler.usecases

import karya.core.entities.Task
import karya.core.locks.LocksClient
import karya.core.queues.QueueClient
import karya.core.repos.RepoConnector
import karya.servers.scheduler.usecases.external.WorkerService
import karya.servers.scheduler.usecases.utils.withNamedContext
import kotlinx.coroutines.channels.ReceiveChannel
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class SchedulerWorker
@Inject
constructor(
  private val workerService: WorkerService,
  private val repoConnector: RepoConnector,
  private val locksClient: LocksClient,
  private val queueClient: QueueClient,
) {
  companion object : Logging

  suspend fun start(instanceId: Int, channel: ReceiveChannel<Task>) {
    logger.info("Starting worker instance $instanceId...")
    withNamedContext("scheduler-karya-worker-$instanceId") { workerService.invoke(channel) }
    logger.info("Worker Instance $instanceId started.")
  }

  suspend fun stop(instanceId: Int) {
    logger.info("Shutting down worker instance $instanceId...")
    repoConnector.shutdown()
    locksClient.shutdown()
    queueClient.shutdown()
    logger.info("Worker Instance $instanceId shutdown complete.")
  }
}
