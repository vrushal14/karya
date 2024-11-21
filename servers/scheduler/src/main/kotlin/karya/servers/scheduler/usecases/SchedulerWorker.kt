package karya.servers.scheduler.usecases

import karya.core.entities.Task
import karya.core.locks.LocksClient
import karya.core.queues.QueueClient
import karya.core.repos.RepoConnector
import karya.servers.scheduler.usecases.external.WorkerService
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.runBlocking
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

		fun start(
			instanceId: Int,
			channel: ReceiveChannel<Task>,
		) {
			logger.info { "Starting worker instance $instanceId..." }
			workerService.start(setWorkerName(instanceId), channel)
			logger.info("Worker Instance $instanceId started.")
		}

		fun stop(instanceId: Int) =
			runBlocking {
				logger.info { "Shutting down worker instance $instanceId..." }
				workerService.stop()
				repoConnector.shutdown()
				locksClient.shutdown()
				queueClient.shutdown()
				logger.info("Worker Instance $instanceId shutdown complete.")
			}

		private fun setWorkerName(instanceId: Int): String {
			val name = "scheduler-karya-worker-$instanceId"
			Thread.currentThread().name = name
			return name
		}
	}
