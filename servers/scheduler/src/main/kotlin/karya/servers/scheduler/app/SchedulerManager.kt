package karya.servers.scheduler.app

import karya.servers.scheduler.configs.SchedulerConfig
import karya.servers.scheduler.di.factories.SchedulerFetcherFactory
import karya.servers.scheduler.di.factories.SchedulerWorkerFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.apache.logging.log4j.kotlin.Logging
import java.util.concurrent.Executors

class SchedulerManager(
	private val config: SchedulerConfig,
) {
	private val dispatcher = Executors.newFixedThreadPool(config.threadCount).asCoroutineDispatcher()
	private val scope = CoroutineScope(dispatcher)
	private val workers = List(config.workers) { SchedulerWorkerFactory.build(config) }
	private val fetcher = SchedulerFetcherFactory.build(config)

	companion object : Logging

	fun start() {
		scope.launch { fetcher.start() }
		workers.forEachIndexed { index, worker ->
			scope.launch { worker.start(index, fetcher.provideChannel()) }
		}
	}

	fun stop() {
		fetcher.stop()
		workers.forEachIndexed { index, worker -> worker.stop(index) }
		scope.cancel()
		dispatcher.close()
	}
}
