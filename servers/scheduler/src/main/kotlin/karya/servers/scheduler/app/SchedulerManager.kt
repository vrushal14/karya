package karya.servers.scheduler.app

import karya.servers.scheduler.usecases.SchedulerFetcher
import karya.servers.scheduler.usecases.SchedulerWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class SchedulerManager(
  private val schedulerFetcher: SchedulerFetcher,
  private val schedulerWorkers: List<SchedulerWorker>,
  private val customDispatcher: ExecutorCoroutineDispatcher
) {
  private val scope = CoroutineScope(customDispatcher)

  fun start() {
    scope.launch { schedulerFetcher.start() }
    schedulerWorkers.forEachIndexed { index, worker ->
      scope.launch { worker.start(index, schedulerFetcher.provideChannel()) }
    }
  }

  fun stop() {
    schedulerFetcher.stop()
    schedulerWorkers.forEachIndexed { index, worker -> worker.stop(index) }
    scope.cancel()
    customDispatcher.close()
  }

}
