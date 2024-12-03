package karya.servers.server.domain.usecases.internal

import karya.core.entities.Action
import karya.core.entities.PlanType
import karya.core.entities.requests.SubmitPlanRequest
import karya.core.exceptions.PlanException.InvalidChainedRequestException
import karya.core.exceptions.PlanException.RecursiveDepthExceededException
import karya.servers.server.configs.ServerConfig
import javax.inject.Inject

class ValidateRequest
@Inject
constructor(
  private val config: ServerConfig
) {

  fun invoke(request: SubmitPlanRequest) {
    if (config.strictMode) {
      checkIfChainedRequestIsRecurring(request)
    }
    checkIfRecursiveDepthIsNotExceeded(request)
  }

  private fun checkIfChainedRequestIsRecurring(request: SubmitPlanRequest) {
    if ((request.action is Action.ChainedRequest).and(request.planType is PlanType.Recurring)) {
      throw InvalidChainedRequestException()
    }
  }

  private fun checkIfRecursiveDepthIsNotExceeded(request: SubmitPlanRequest) {
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
