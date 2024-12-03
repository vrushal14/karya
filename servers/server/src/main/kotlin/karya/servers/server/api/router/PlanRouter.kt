package karya.servers.server.api.router

import io.ktor.server.routing.*
import karya.servers.server.api.service.*
import javax.inject.Inject
import javax.inject.Provider

class PlanRouter
@Inject
constructor(
  private val submitPlanService: Provider<SubmitPlanService>,
  private val getPlanService: Provider<GetPlanService>,
  private val updatePlanService: Provider<UpdatePlanService>,
  private val cancelPlanService: Provider<CancelPlanService>,
  private val getSummaryService: Provider<GetSummaryService>
) {
  fun Route.wireRoutes() {
    patch { updatePlanService.get().invoke(call) }
    post { submitPlanService.get().invoke(call) }
    route("{plan_id}") {
      get { getPlanService.get().invoke(call) }
      get("summary") { getSummaryService.get().invoke(call) }
      post { cancelPlanService.get().invoke(call) }
    }
  }
}
