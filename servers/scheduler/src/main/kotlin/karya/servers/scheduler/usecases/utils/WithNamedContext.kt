package karya.servers.scheduler.usecases.utils

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.withContext

/**
 * Executes a given block of code within a coroutine context that has a specified name.
 *
 * @param name The name to be assigned to the coroutine context.
 * @param block The block of code to be executed within the named context.
 * @return The result of the block execution.
 */
suspend fun <T> withNamedContext(name: String, block: suspend () -> T): T {
  return withContext(CoroutineName(name)) {
    block()
  }
}
