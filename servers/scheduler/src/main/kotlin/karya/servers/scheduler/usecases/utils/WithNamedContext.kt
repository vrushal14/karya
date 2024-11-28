package karya.servers.scheduler.usecases.utils

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.withContext

suspend fun <T> withNamedContext(name: String, block: suspend () -> T): T {
  return withContext(CoroutineName(name)) {
    block()
  }
}
