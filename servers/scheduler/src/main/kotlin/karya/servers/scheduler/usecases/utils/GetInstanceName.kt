package karya.servers.scheduler.usecases.utils

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.currentCoroutineContext

/**
 * Utility function to get the name of the current coroutine instance.
 *
 * @return The name of the current coroutine instance, or "Unknown" if the name is not set.
 */
suspend fun getInstanceName(): String {
  return currentCoroutineContext()[CoroutineName]?.name ?: "Unknown"
}
