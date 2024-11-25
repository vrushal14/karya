package karya.servers.server.api.mapper

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import karya.core.exceptions.JobException
import karya.core.exceptions.KaryaException
import karya.core.exceptions.LocksException
import karya.core.exceptions.TaskException
import karya.core.exceptions.UserException

suspend fun KaryaException.toHttpResponse(call: ApplicationCall) =
  when (this) {
    is UserException.UserNotFoundException -> call.respond(HttpStatusCode.NotFound, this)

    is JobException.UnknownJobTypeException -> call.respond(HttpStatusCode.InternalServerError, this)
    is JobException.UnknownJobStatusException -> call.respond(HttpStatusCode.InternalServerError, this)
    is JobException.JobNotFoundException -> call.respond(HttpStatusCode.NotFound, this)

    is TaskException.UnknownTaskStatusException -> call.respond(HttpStatusCode.InternalServerError, this)
    is TaskException.TaskNotCreatedException -> call.respond(HttpStatusCode.InternalServerError, this)

    is LocksException.UnableToAcquireLockException -> call.respond(HttpStatusCode.ServiceUnavailable, this)

    else -> call.respond(HttpStatusCode.UnprocessableEntity, this)
  }
