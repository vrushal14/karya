package karya.data.psql.configs

import karya.core.configs.RepoConfig
import java.util.Properties
import kotlin.collections.component1

data class PsqlRepoConfig(
  val hikariProperties : Properties,
  val flywayProperties : Properties,
  override val partitions: Int
) : RepoConfig(PSQL_IDENTIFIER, partitions) {

  companion object {

    const val PSQL_IDENTIFIER = "psql"

    fun extractProperties(repo: Map<*, *>, key: String): Properties {
      val propertiesMap = (repo[key] as? Map<*, *>)
        ?: throw IllegalArgumentException("Missing or invalid properties for '$key'")
      return Properties().apply {
        propertiesMap.forEach { (k, v) -> setProperty(k.toString(), v.toString()) }
      }
    }
  }

  constructor(props: Map<*,*>, partitions: Int) : this(
    hikariProperties = extractProperties(props, "hikari"),
    flywayProperties = extractProperties(props, "flyway"),
    partitions = partitions
  )
}
