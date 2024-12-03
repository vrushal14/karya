package karya.servers.executor.usecase.external

import karya.core.queues.entities.QueueMessage
import karya.core.queues.entities.QueueMessage.ExecutorMessage
import karya.core.queues.entities.QueueMessage.HookMessage
import javax.inject.Inject

class ProcessMessage
@Inject
constructor(
  private val processExecutorMessage: ProcessExecutorMessage,
  private val processHookMessage: ProcessHookMessage
) {

  suspend fun invoke(message: QueueMessage) = when (message) {
    is ExecutorMessage -> processExecutorMessage.invoke(message)
    is HookMessage -> processHookMessage.invoke(message)
  }
}
