package karya.servers.server.api.service

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import karya.core.entities.requests.UpdatePlanRequest
import karya.core.exceptions.KaryaException
import karya.servers.server.api.mapper.toHttpResponse
import karya.servers.server.domain.usecases.external.UpdatePlan
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

/**
 * Service class responsible for handling update plan requests.
 *
 * @property updatePlan The use case for updating a plan.
 */
class UpdatePlanService
@Inject
constructor(
  private val updatePlan: UpdatePlan,
) {
  companion object : Logging

  /**
   * Handles the update plan request.
   *
   * Receives the update plan request, invokes the update plan use case, and responds with the result.
   * If an exception occurs, it logs the error and responds with the appropriate HTTP error response.
   *
   * @param call The application call instance.
   */
  suspend fun invoke(call: ApplicationCall) =
    try {
      val request = call.receive<UpdatePlanRequest>()
      val response = updatePlan.invoke(request)
      call.respond(HttpStatusCode.OK, response)
    } catch (e: KaryaException) {
      logger.error(e)
      e.toHttpResponse(call)
    }
}
