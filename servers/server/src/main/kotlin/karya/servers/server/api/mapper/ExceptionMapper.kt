package karya.servers.server.api.mapper

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import karya.core.exceptions.*
import karya.core.exceptions.JobException
import karya.core.exceptions.TaskException
import karya.core.exceptions.UserException

suspend fun KaryaException.toHttpResponse(call: ApplicationCall) =
  when (this) {
    is UserException.UserNotFoundException -> call.respond(HttpStatusCode.NotFound, this.)

    is JobException.UnknownJobTypeException -> call.respond(HttpStatusCode.InternalServerError, this.message)
    is JobException.UnknownJobStatusException -> call.respond(HttpStatusCode.InternalServerError, this.message)
    is JobException.JobNotFoundException -> call.respond(HttpStatusCode.NotFound, this.message)

    is TaskException.UnknownTaskStatusException -> call.respond(HttpStatusCode.InternalServerError, this.message)
    is TaskException.TaskNotCreatedException -> call.respond(HttpStatusCode.InternalServerError, this.message)

    is LocksException.UnableToAcquireLockException -> call.respond(HttpStatusCode.ServiceUnavailable, this.message)

    else -> call.respond(HttpStatusCode.UnprocessableEntity, this)
  }
