package karya.servers.server.api.service

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.util.*
import karya.core.exceptions.KaryaException
import karya.servers.server.api.mapper.toHttpResponse
import karya.servers.server.domain.usecases.GetJob
import org.apache.logging.log4j.kotlin.Logging
import java.util.*
import javax.inject.Inject

class GetJobService
@Inject
constructor(
  private val getJob : GetJob
){

  companion object: Logging

  suspend fun invoke(call: ApplicationCall) = try {
    val params = call.parameters
    val jobId = UUID.fromString(params.getOrFail("job_id"))
    val response = getJob.invoke(jobId)
    call.respond(HttpStatusCode.OK, response)

  } catch (e : KaryaException) {
    logger.error(e)
    e.toHttpResponse(call)
  }
}