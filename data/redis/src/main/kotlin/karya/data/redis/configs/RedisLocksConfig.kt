package karya.data.redis.configs

data class RedisLocksConfig(
  val url : String,
  val waitTime : Long,
  val leaseTime : Long
)
