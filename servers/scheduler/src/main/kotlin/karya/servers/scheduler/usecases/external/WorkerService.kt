package karya.servers.scheduler.usecases.external

import karya.core.entities.Task
import karya.servers.scheduler.usecases.internal.ProcessTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import org.apache.logging.log4j.kotlin.Logging
import java.util.concurrent.Executors
import javax.inject.Inject

class WorkerService
	@Inject
	constructor(
		private val processTask: ProcessTask,
	) {
		companion object : Logging

		private lateinit var scope: CoroutineScope
		private lateinit var customDispatcher: ExecutorCoroutineDispatcher

		fun start(
			name: String,
			channel: ReceiveChannel<Task>,
		) {
			setScope(name)
			scope.launch {
				try {
					for (task in channel) processTask.invoke(task)
				} catch (e: ClosedReceiveChannelException) {
					logger.info { "Task channel closed. Shutting down worker." }
					stop()
				}
			}
		}

		fun stop() {
			scope.cancel()
			customDispatcher.close()
		}

		private fun setScope(name: String) {
			this.customDispatcher =
				Executors
					.newSingleThreadExecutor { runnable ->
						Thread(runnable, name)
					}.asCoroutineDispatcher()
			this.scope = CoroutineScope(customDispatcher + SupervisorJob())
		}
	}
