package karya.servers.server.domain.usecases.internal

import karya.core.entities.JobType
import karya.core.entities.action.Action
import karya.core.entities.requests.SubmitJobRequest
import karya.core.exceptions.JobException.InvalidChainedRequestException
import karya.core.exceptions.JobException.RecursiveDepthExceededException
import karya.servers.server.configs.ServerConfig
import javax.inject.Inject

class ValidateRequest
@Inject
constructor(
  private val config: ServerConfig
) {

  fun invoke(request: SubmitJobRequest) {
    if (config.strictMode) {
      checkIfChainedRequestIsRecurring(request)
    }
    checkIfRecursiveDepthIsNotExceeded(request)
  }

  private fun checkIfChainedRequestIsRecurring(request: SubmitJobRequest) {
    if ((request.action is Action.ChainedRequest).and(request.jobType is JobType.Recurring)) {
      throw InvalidChainedRequestException()
    }
  }

  private fun checkIfRecursiveDepthIsNotExceeded(request: SubmitJobRequest) {
    if (request.action is Action.ChainedRequest) {
      var depth = 1
      var currentAction: Action = request.action
      while (currentAction is Action.ChainedRequest) {
        if (depth > config.maxChainedDepth) {
          throw RecursiveDepthExceededException(depth, config.maxChainedDepth)
        }
        depth++
        currentAction = currentAction.request.action
      }
    }
  }
}
