package karya.servers.server.api.router

import io.ktor.server.routing.*
import karya.servers.server.api.service.*
import javax.inject.Inject
import javax.inject.Provider

class JobRouter
@Inject
constructor(
  private val submitJobService: Provider<SubmitJobService>,
  private val getJobService: Provider<GetJobService>,
  private val updateJobService: Provider<UpdateJobService>,
  private val cancelJobService: Provider<CancelJobService>,
  private val getSummaryService: Provider<GetSummaryService>
) {
  fun Route.wireRoutes() {
    patch { updateJobService.get().invoke(call) }
    post { submitJobService.get().invoke(call) }
    route("{job_id}") {
      get { getJobService.get().invoke(call) }
      get("summary") { getSummaryService.get().invoke(call) }
      post { cancelJobService.get().invoke(call) }
    }
  }
}
