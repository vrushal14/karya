package karya.servers.server.api.service

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import karya.core.entities.requests.SubmitJobRequest
import karya.core.exceptions.KaryaException
import karya.servers.server.api.mapper.toHttpResponse
import karya.servers.server.domain.usecases.external.SubmitJob
import karya.servers.server.domain.usecases.internal.ValidateRequest
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class SubmitJobService
@Inject
constructor(
  private val submitJob: SubmitJob,
  private val validateRequest: ValidateRequest
) {
  companion object : Logging

  suspend fun invoke(call: ApplicationCall) =
    try {
      val request = call.receive<SubmitJobRequest>()
      validateRequest.invoke(request)
      val response = submitJob.invoke(request, parentJobId = null)
      call.respond(HttpStatusCode.OK, response)
    } catch (e: KaryaException) {
      logger.error(e)
      e.toHttpResponse(call)
    }
}
