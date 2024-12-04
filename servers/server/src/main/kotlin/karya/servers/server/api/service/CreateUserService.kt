package karya.servers.server.api.service

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import karya.core.entities.requests.CreateUserRequest
import karya.core.exceptions.KaryaException
import karya.servers.server.api.mapper.toHttpResponse
import karya.servers.server.domain.usecases.external.CreateUser
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

/**
 * Service class responsible for handling create user requests.
 *
 * @property createUser The use case for creating a user.
 */
class CreateUserService
@Inject
constructor(
  private val createUser: CreateUser,
) {
  companion object : Logging

  /**
   * Handles the create user request.
   *
   * Receives the create user request, invokes the create user use case, and responds with the result.
   * If an exception occurs, it logs the error and responds with the appropriate HTTP error response.
   *
   * @param call The application call instance.
   */
  suspend fun invoke(call: ApplicationCall) =
    try {
      val request = call.receive<CreateUserRequest>()
      val result = createUser.invoke(request)
      call.respond(HttpStatusCode.OK, result)
    } catch (e: KaryaException) {
      logger.error(e)
      e.toHttpResponse(call)
    }
}
