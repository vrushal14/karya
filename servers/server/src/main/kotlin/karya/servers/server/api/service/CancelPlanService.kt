package karya.servers.server.api.service

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.util.*
import karya.core.exceptions.KaryaException
import karya.servers.server.api.mapper.toHttpResponse
import karya.servers.server.domain.usecases.external.CancelPlan
import org.apache.logging.log4j.kotlin.Logging
import java.util.*
import javax.inject.Inject

/**
 * Service class responsible for handling cancel plan requests.
 *
 * @property cancelPlan The use case for canceling a plan.
 */
class CancelPlanService
@Inject
constructor(
  private val cancelPlan: CancelPlan,
) {
  companion object : Logging

  /**
   * Handles the cancel plan request.
   *
   * Receives the cancel plan request, invokes the cancel plan use case, and responds with the result.
   * If an exception occurs, it logs the error and responds with the appropriate HTTP error response.
   *
   * @param call The application call instance.
   */
  suspend fun invoke(call: ApplicationCall) =
    try {
      val params = call.parameters
      val planId = UUID.fromString(params.getOrFail("plan_id"))
      val response = cancelPlan.invoke(planId)
      call.respond(HttpStatusCode.OK, response)
    } catch (e: KaryaException) {
      logger.error(e)
      e.toHttpResponse(call)
    }
}
