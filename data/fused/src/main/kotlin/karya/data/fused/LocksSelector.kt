package karya.data.fused

import karya.core.configs.LocksConfig
import karya.data.fused.exceptions.UnknownProviderException
import karya.data.fused.utils.getSection
import karya.data.redis.configs.RedisLocksConfig
import kotlin.collections.get

object LocksSelector {
  fun get(filePath : String) : LocksConfig {
    val section = getSection(filePath, "lock")

    return when(val provider = section["provider"]) {
      "redis" -> RedisLocksConfig(section["properties"] as Map<*,*>)

      else -> throw UnknownProviderException("repo", provider.toString())
    }
  }
}