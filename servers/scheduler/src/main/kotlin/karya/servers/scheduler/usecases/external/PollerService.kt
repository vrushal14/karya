package karya.servers.scheduler.usecases.external

import karya.servers.scheduler.configs.SchedulerConfig
import karya.servers.scheduler.usecases.internal.ProcessTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import org.apache.logging.log4j.kotlin.Logging
import java.time.Duration
import java.util.concurrent.Executors
import javax.inject.Inject

class PollerService
@Inject
constructor(
  private val config: SchedulerConfig,
  private val processTask: ProcessTask
) {

  companion object : Logging

  private lateinit var scope : CoroutineScope
  private lateinit var customDispatcher: ExecutorCoroutineDispatcher

  // Flow that emits a value at a fixed interval and calls the I/O function
  private val pollFlow = flow {
    while (true) {
      emit(processTask.invoke())
      delay(Duration.ofMillis(config.pollFrequency))
    }
  }

  fun start(name : String) {
    setScope(name)
    scope.launch {
      pollFlow
        .catch { e -> logger.error("Error during polling: ${e.message}") }
        .collect { res -> if (!res) logger.info { "Poller fetched 0 tasks..." } }
    }
  }

  fun stop() {
    scope.cancel()
    customDispatcher.close()
  }

  private fun setScope(name: String) {
    this.customDispatcher = Executors.newSingleThreadExecutor { runnable ->
      Thread(runnable, name)
    }.asCoroutineDispatcher()
    this.scope = CoroutineScope(customDispatcher + SupervisorJob())
  }
}