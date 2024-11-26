package karya.data.redis.configs

import karya.core.configs.LocksConfig
import karya.core.utils.readValue

data class RedisLocksConfig(
  val hostname: String,
  val port: Int,
  val waitTime: Long,
  val leaseTime: Long,
) : LocksConfig(REDIS_IDENTIFIER) {

  companion object {
    const val REDIS_IDENTIFIER = "redis"
  }

  constructor(props: Map<*, *>) : this(
    hostname = props.readValue("hostname"),
    port = props.readValue("port"),
    leaseTime = props.readValue("leaseTime"),
    waitTime = props.readValue("waitTime"),
  )

  fun getUrl() = "redis://$hostname:$port"
}
