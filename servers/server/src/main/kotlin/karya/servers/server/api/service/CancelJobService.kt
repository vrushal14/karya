package karya.servers.server.api.service

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import io.ktor.server.util.getOrFail
import karya.core.exceptions.KaryaException
import karya.servers.server.api.mapper.toHttpResponse
import karya.servers.server.domain.usecases.CancelJob
import org.apache.logging.log4j.kotlin.Logging
import java.util.*
import javax.inject.Inject

class CancelJobService
@Inject
constructor(
    private val cancelJob: CancelJob,
) {
    companion object : Logging

    suspend fun invoke(call: ApplicationCall) =
        try {
            val params = call.parameters
            val jobId = UUID.fromString(params.getOrFail("job_id"))
            val response = cancelJob.invoke(jobId)
            call.respond(HttpStatusCode.OK, response)
        } catch (e: KaryaException) {
            logger.error(e)
            e.toHttpResponse(call)
        }
}
