package karya.data.rabbitmq.configs

object ExchangeConfig {
  private const val PROJECT = "karya"

  const val EXCHANGE_TYPE = "direct"
  const val EXCHANGE_NAME = "$PROJECT-exchange"
  const val DL_EXCHANGE_NAME = "$PROJECT-dead-letter-exchange"

  const val EXECUTOR_ROUTING_KEY = "$PROJECT-executor-key"
  const val EXECUTOR_QUEUE_NAME = "$PROJECT-executor-queue"

  const val DL_ROUTING_KEY = "$PROJECT-dead-letter-key"
  const val DL_QUEUE_NAME = "$PROJECT-dead-letter-queue"

  const val HOOKS_ROUTING_KEY = "$PROJECT-hooks-key"
  const val HOOKS_QUEUE_NAME = "$PROJECT-hooks-queue"
}
