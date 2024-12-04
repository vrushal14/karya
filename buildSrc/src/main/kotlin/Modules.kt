object Modules {
  const val CORE = ":core"
  const val CLIENT = ":client"

  object Data {
    private const val DATA = ":data"

    const val PSQL = "$DATA-psql"
    const val FUSED = "$DATA-fused"
    const val REDIS = "$DATA-redis"
    const val RABBIT_MQ = "$DATA-rabbitmq"
  }

  object Servers {
    private const val SERVERS = ":servers"

    const val SERVER = "$SERVERS-server"
    const val SCHEDULER = "$SERVERS-scheduler"
    const val EXECUTOR = "$SERVERS-executor"
  }

  object Connectors {
    private const val CONNECTORS = ":connectors"

    const val REST_API = "$CONNECTORS-restapi"
    const val CHAINED_PLAN = "$CONNECTORS-chainedplan"
    const val SLACK_MESSAGE = "$CONNECTORS-slackmessage"
  }
}
