package karya.data.fused

import karya.core.configs.LocksConfig
import karya.core.utils.getSection
import karya.data.fused.exceptions.FusedDataException.UnknownProviderException
import karya.data.redis.configs.RedisLocksConfig
import karya.data.redis.configs.RedisLocksConfig.Companion.REDIS_IDENTIFIER

/**
 * Object responsible for selecting the appropriate locks configuration
 * based on the provided file path.
 */
object LocksSelector {

  /**
   * Retrieves the locks configuration from the specified file path.
   *
   * @param filePath The path to the configuration file.
   * @return The locks configuration.
   * @throws UnknownProviderException If the provider specified in the configuration is unknown.
   */
  fun get(filePath: String): LocksConfig {
    val section: Map<String, *> = getSection(filePath, "lock")
    val properties = section["properties"] as Map<*, *>

    return when (val provider = section["provider"]) {
      REDIS_IDENTIFIER -> RedisLocksConfig(properties)

      else -> throw UnknownProviderException("lock", provider.toString())
    }
  }
}
