package karya.data.fused.repos

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import karya.data.fused.exceptions.UnknownProviderException
import java.io.File
import java.util.Properties

sealed class RepoConfig {

  data class Psql(
    val partitions: Int,
    val hikariProperties: Properties,
    val flywayProperties: Properties
  ) : RepoConfig()

  companion object {
    fun fromYaml(filePath: String): RepoConfig {
      val mapper = ObjectMapper(YAMLFactory()).apply { findAndRegisterModules() }
      val yamlMap: Map<String, Any> = mapper.readValue(File(filePath), Map::class.java) as Map<String, Any>

      val repo = yamlMap["repo"] as? Map<*, *> ?: throw IllegalArgumentException("Missing 'repo' configuration")
      val provider = repo["provider"] as? String ?: throw IllegalArgumentException("Missing 'provider' in repo configuration")

      return when (provider) {
        "psql" -> {
          val partitions = (repo["partitions"] as? Number)?.toInt() ?: throw IllegalArgumentException("Missing or invalid 'partitions'")

          val hikariProperties = extractProperties(repo, "hikari")
          val flywayProperties = extractProperties(repo, "flyway")

          Psql(partitions, hikariProperties, flywayProperties)
        }
        else -> throw UnknownProviderException("repo", provider)
      }
    }

    private fun extractProperties(repo: Map<*, *>, key: String): Properties {
      val propertiesMap = (repo["properties"] as? Map<*, *>)?.get(key) as? Map<*, *>
        ?: throw IllegalArgumentException("Missing or invalid properties for '$key'")

      return Properties().apply {
        propertiesMap.forEach { (k, v) -> setProperty(k.toString(), v.toString()) }
      }
    }
  }

}
