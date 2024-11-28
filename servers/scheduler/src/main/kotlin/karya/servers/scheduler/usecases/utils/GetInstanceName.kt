package karya.servers.scheduler.usecases.utils

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.currentCoroutineContext

suspend fun getInstanceName(): String {
  return currentCoroutineContext()[CoroutineName]?.name ?: "Unknown"
}
