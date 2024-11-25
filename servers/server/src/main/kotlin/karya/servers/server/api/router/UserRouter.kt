package karya.servers.server.api.router

import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import karya.servers.server.api.service.CreateUserService
import javax.inject.Inject
import javax.inject.Provider

class UserRouter
@Inject
constructor(
  private val createUserService: Provider<CreateUserService>,
) {
  fun Route.wireRoutes() {
    post { createUserService.get().invoke(call) }
  }
}
