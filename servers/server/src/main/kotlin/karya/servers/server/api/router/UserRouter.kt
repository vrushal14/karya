package karya.servers.server.api.router

import io.ktor.server.routing.*
import karya.servers.server.api.service.CreateUserService
import javax.inject.Inject
import javax.inject.Provider

/**
 * Router class responsible for wiring user-related routes.
 *
 * @property createUserService Provider for the CreateUserService.
 */
class UserRouter
@Inject
constructor(
  private val createUserService: Provider<CreateUserService>,
) {
  /**
   * Wires the routes for user-related operations.
   *
   * Defines the POST route for creating a user and delegates the handling to the CreateUserService.
   */
  fun Route.wireRoutes() {
    post { createUserService.get().invoke(call) }
  }
}
