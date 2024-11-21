package karya.servers.server.app.ktor

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import java.time.Duration
import javax.inject.Inject

class KtorServer
	@Inject
	constructor(
		private val featureManager: FeatureManager,
		private val routeManager: RouteManager,
	) {
		private val engine = buildEngine()

		fun start() {
			engine.start(false)
		}

		fun stop() {
			val gracePeriod = Duration.ofSeconds(5).toMillis()
			val timeout = Duration.ofSeconds(15).toMillis()
			engine.stop(gracePeriod, timeout)
		}

		private fun buildEngine() = embeddedServer(CIO, port = 8080, module = { main() })

		private fun Application.main() {
			featureManager.apply { wireFeatures() }
			routeManager.apply { wireRoutes() }
		}
	}
