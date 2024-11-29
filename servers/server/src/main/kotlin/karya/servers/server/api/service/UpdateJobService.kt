package karya.servers.server.api.service

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import karya.core.entities.requests.UpdateJobRequest
import karya.core.exceptions.KaryaException
import karya.servers.server.api.mapper.toHttpResponse
import karya.servers.server.domain.usecases.external.UpdateJob
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class UpdateJobService
@Inject
constructor(
  private val updateJob: UpdateJob,
) {
  companion object : Logging

  suspend fun invoke(call: ApplicationCall) =
    try {
      val request = call.receive<UpdateJobRequest>()
      val response = updateJob.invoke(request)
      call.respond(HttpStatusCode.OK, response)
    } catch (e: KaryaException) {
      logger.error(e)
      e.toHttpResponse(call)
    }
}
