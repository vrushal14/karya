plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "Karya"

include(":core")
project(":core").projectDir = File("core")

include(":client")
project(":client").projectDir = File("client")

include(":servers-server")
project(":servers-server").projectDir = File("servers/server")

include(":servers-scheduler")
project(":servers-scheduler").projectDir = File("servers/scheduler")

include("servers-worker")
project(":servers-worker").projectDir = File("servers/worker")

include(":data-fused")
project(":data-fused").projectDir = File("data/fused")

include(":data-psql")
project(":data-psql").projectDir = File("data/psql")

include(":data-redis")
project(":data-redis").projectDir = File("data/redis")

include(":data-rabbitmq")
project(":data-rabbitmq").projectDir = File("data/rabbitmq")

include(":docs-samples")
project(":docs-samples").projectDir = File("docs/samples")
