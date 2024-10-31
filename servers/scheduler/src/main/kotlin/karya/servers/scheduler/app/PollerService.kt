package karya.servers.scheduler.app

import karya.servers.scheduler.configs.SchedulerConfig
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.time.delay
import org.apache.logging.log4j.kotlin.Logging
import java.time.Duration


class PollerService<Task> (
  private val config: SchedulerConfig, // Frequency for polling
  private val ioCall: suspend () -> Task?,   // I/O function to be called
  private val onResult: suspend (Task) -> Unit      // Callback to process each result
) {

  companion object : Logging

  private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

  // Flow that emits a value at a fixed interval and calls the I/O function
  private val pollFlow = flow {
    while (true) {
      emit(ioCall())
      delay(Duration.ofMillis(config.pollFrequency))
    }
  }

  fun start() {
    scope.launch {
      pollFlow.catch { e ->
        println("[${config.getName()}] --- Error during polling: ${e.message}")
      }
        .collect { result ->
          if (result != null) {
            onResult(result)
          } else logger.info { "[${config.getName()}] --- Poller fetched 0 tasks..." }
        }
    }
  }

  fun stop() {
    scope.cancel()
  }
}