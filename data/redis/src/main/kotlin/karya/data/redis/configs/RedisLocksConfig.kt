package karya.data.redis.configs

import karya.core.configs.LocksConfig
import karya.core.utils.PropsReader.readValue

data class RedisLocksConfig(
  val url : String,
  val waitTime : Long,
  val leaseTime : Long
) : LocksConfig("redis") {

  constructor(props : Map<*,*>) : this(
    url = props.readValue("url"),
    leaseTime = props.readValue("leaseTime"),
    waitTime = props.readValue("waitTime")
  )
}
