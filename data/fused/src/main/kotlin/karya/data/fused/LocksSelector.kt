package karya.data.fused

import karya.core.configs.LocksConfig
import karya.data.fused.exceptions.UnknownProviderException
import karya.data.fused.utils.getSection
import karya.data.redis.configs.RedisLocksConfig

object LocksSelector {

  private const val REDIS = "redis"

  fun get(filePath : String) : LocksConfig {
    val section = getSection(filePath, "lock")
    val properties = section["properties"] as Map<*,*>
    return when(val provider = section["provider"]) {
      REDIS -> RedisLocksConfig(properties)

      else -> throw UnknownProviderException("lock", provider.toString())
    }
  }
}