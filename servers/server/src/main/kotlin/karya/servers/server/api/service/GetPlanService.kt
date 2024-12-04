package karya.servers.server.api.service

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.util.*
import karya.core.exceptions.KaryaException
import karya.servers.server.api.mapper.toHttpResponse
import karya.servers.server.domain.usecases.external.GetPlan
import org.apache.logging.log4j.kotlin.Logging
import java.util.*
import javax.inject.Inject

/**
 * Service class responsible for handling get plan requests.
 *
 * @property getPlan The use case for getting a plan.
 */
class GetPlanService
@Inject
constructor(
  private val getPlan: GetPlan,
) {
  companion object : Logging

  /**
   * Handles the get plan request.
   *
   * Receives the get plan request, invokes the get plan use case, and responds with the result.
   * If an exception occurs, it logs the error and responds with the appropriate HTTP error response.
   *
   * @param call The application call instance.
   */
  suspend fun invoke(call: ApplicationCall) = try {
    val params = call.parameters
    val planId = UUID.fromString(params.getOrFail("plan_id"))
    val response = getPlan.invoke(planId)
    call.respond(HttpStatusCode.OK, response)

  } catch (e: KaryaException) {
    logger.error(e)
    e.toHttpResponse(call)
  }
}
