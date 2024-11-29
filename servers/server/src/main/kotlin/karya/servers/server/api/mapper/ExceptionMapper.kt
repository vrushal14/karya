package karya.servers.server.api.mapper

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import karya.core.exceptions.*

suspend fun KaryaException.toHttpResponse(call: ApplicationCall) =
  when (this) {
    is UserException.UserNotFoundException -> call.respond(HttpStatusCode.NotFound, this.message)

    is JobException.UnknownJobTypeException -> call.respond(HttpStatusCode.InternalServerError, this.message)
    is JobException.UnknownJobStatusException -> call.respond(HttpStatusCode.InternalServerError, this.message)
    is JobException.JobNotFoundException -> call.respond(HttpStatusCode.NotFound, this.message)
    is JobException.InvalidChainedRequestException -> call.respond(HttpStatusCode.BadRequest, this.message)
    is JobException.RecursiveDepthExceededException -> call.respond(HttpStatusCode.BadRequest, this.message)

    is TaskException.UnknownTaskStatusException -> call.respond(HttpStatusCode.InternalServerError, this.message)
    is TaskException.TaskNotCreatedException -> call.respond(HttpStatusCode.InternalServerError, this.message)

    is LocksException.UnableToAcquireLockException -> call.respond(HttpStatusCode.ServiceUnavailable, this.message)

    else -> call.respond(HttpStatusCode.UnprocessableEntity, this)
  }
