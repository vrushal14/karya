plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
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

include("servers-executor")
project(":servers-executor").projectDir = File("servers/executor")

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

include(":connectors-restapi")
project(":connectors-restapi").projectDir = File("connectors/restapi")

include("connectors-chainedplan")
project(":connectors-chainedplan").projectDir = File("connectors/chainedplan")

include("connectors-slack")
project(":connectors-slack").projectDir = File("connectors/slack")

include("connectors-email")
project(":connectors-email").projectDir = File("connectors/email")

include("connectors-kafka")
project(":connectors-kafka").projectDir = File("connectors/kafka")
