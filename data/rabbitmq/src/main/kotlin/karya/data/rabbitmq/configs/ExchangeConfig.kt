package karya.data.rabbitmq.configs

object ExchangeConfig {
  const val EXCHANGE_TYPE = "direct"

  const val EXCHANGE_NAME = "karya-exchange"
  const val ROUTING_KEY = "task-executor"
  const val QUEUE_NAME = "karya-executor-queue"

  const val DL_EXCHANGE_NAME = "karya-dlx-exchange"
  const val DL_ROUTING_KEY = "dead-letter-key"
  const val DL_QUEUE_NAME = "dead-letter-queue"
}
