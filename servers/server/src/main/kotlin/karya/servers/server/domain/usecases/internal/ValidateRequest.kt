package karya.servers.server.domain.usecases.internal

import karya.core.entities.Action
import karya.core.entities.PlanType
import karya.core.entities.requests.SubmitPlanRequest
import karya.core.exceptions.PlanException.InvalidChainedRequestException
import karya.core.exceptions.PlanException.RecursiveDepthExceededException
import karya.servers.server.configs.ServerConfig
import javax.inject.Inject

/**
 * Use case for validating a submit plan request.
 *
 * @property config The server configuration.
 */
class ValidateRequest
@Inject
constructor(
  private val config: ServerConfig
) {

  /**
   * Validates the given submit plan request.
   *
   * @param request The submit plan request to validate.
   * @throws InvalidChainedRequestException If the request is a chained request and the plan type is recurring.
   * @throws RecursiveDepthExceededException If the depth of the chained request exceeds the max allowed depth.
   */
  fun invoke(request: SubmitPlanRequest) {
    if (config.strictMode) {
      checkIfChainedRequestIsRecurring(request)
    }
    checkIfRecursiveDepthIsNotExceeded(request)
  }

  /**
   * Checks if the chained request is recurring and throws an exception if it is.
   *
   * @param request The submit plan request to check.
   * @throws InvalidChainedRequestException If the request is a chained request and the plan type is recurring.
   */
  private fun checkIfChainedRequestIsRecurring(request: SubmitPlanRequest) {
    if ((request.action is Action.ChainedRequest).and(request.planType is PlanType.Recurring)) {
      throw InvalidChainedRequestException()
    }
  }

  /**
   * Checks if the depth of the chained request exceeds the maximum allowed depth and throws an exception if it does.
   *
   * @param request The submit plan request to check.
   * @throws RecursiveDepthExceededException If the depth of the chained request exceeds the maximum allowed depth.
   */
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
