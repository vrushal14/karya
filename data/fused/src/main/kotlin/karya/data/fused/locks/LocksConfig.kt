package karya.data.fused.locks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import karya.data.fused.exceptions.UnknownProviderException
import karya.data.redis.configs.RedisLocksConfig
import java.io.File

sealed class LocksConfig {

  data class Redis(
    val redisLocksConfig: RedisLocksConfig
  ) : LocksConfig()

  companion object {
    fun fromYaml(filePath: String): LocksConfig {
      val mapper = ObjectMapper(YAMLFactory()).apply { findAndRegisterModules() }
      val yamlContent = File(filePath).readText()
      val yamlMap: Map<String, Any> = mapper.readValue(yamlContent, Map::class.java) as Map<String, Any>

      val repo = yamlMap["locks"] as? Map<*,*>
      return when(val provider = repo?.get("provider")) {
        "redis" -> {
          val props = repo["properties"] as Map<*,*>
          Redis(redisLocksConfig = RedisLocksConfig(
              url = props["url"] as? String ?: throw IllegalArgumentException("URL must be provided"),
              leaseTime = (props["leaseTime"] as? Number)?.toLong() ?: throw IllegalArgumentException("Lease time must be provided"),
              waitTime = (props["waitTime"] as? Number)?.toLong()  ?: throw IllegalArgumentException("Wait time must be provided")
            )
          )
        }
        else -> throw UnknownProviderException("repo", provider.toString())
      }
    }
  }
}