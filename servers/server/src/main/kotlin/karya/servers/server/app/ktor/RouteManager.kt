package karya.servers.server.app.ktor

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import karya.servers.server.api.router.JobRouter
import karya.servers.server.api.router.UserRouter
import javax.inject.Inject

class RouteManager
@Inject
constructor(
  private val userRouter: UserRouter,
  private val jobRouter: JobRouter,
) {
  fun Application.wireRoutes() {
    routing {
      get { call.respond(HttpStatusCode.OK, Unit) }

      route("v1") {
        route("user") { userRouter.apply { wireRoutes() } }
        route("job") { jobRouter.apply { wireRoutes() } }
      }
    }
  }
}
