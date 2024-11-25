package karya.servers.server.api.service

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import karya.core.entities.requests.UpdateJobRequest
import karya.core.exceptions.KaryaException
import karya.servers.server.api.mapper.toHttpResponse
import karya.servers.server.domain.usecases.UpdateJob
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
