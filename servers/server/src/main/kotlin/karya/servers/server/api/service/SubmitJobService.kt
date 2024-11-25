package karya.servers.server.api.service

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import karya.core.entities.requests.SubmitJobRequest
import karya.core.exceptions.KaryaException
import karya.servers.server.api.mapper.toHttpResponse
import karya.servers.server.domain.usecases.SubmitJob
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class SubmitJobService
@Inject
constructor(
  private val submitJob: SubmitJob,
) {
  companion object : Logging

  suspend fun invoke(call: ApplicationCall) =
    try {
      val request = call.receive<SubmitJobRequest>()
      val response = submitJob.invoke(request)
      call.respond(HttpStatusCode.OK, response)
    } catch (e: KaryaException) {
      logger.error(e)
      e.toHttpResponse(call)
    }
}
