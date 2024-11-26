package karya.data.fused

import karya.core.configs.RepoConfig
import karya.core.utils.getSection
import karya.core.utils.readValue
import karya.data.fused.exceptions.UnknownProviderException
import karya.data.psql.configs.PsqlRepoConfig
import karya.data.psql.configs.PsqlRepoConfig.Companion.PSQL_IDENTIFIER

object RepoSelector {
  fun get(filePath: String): RepoConfig {
    val section: Map<String, *> = getSection(filePath, "repo")
    val properties = section["properties"] as Map<*, *>
    return when (val provider = section["provider"]) {
      PSQL_IDENTIFIER -> PsqlRepoConfig(properties, section.readValue("partitions"))

      else -> throw UnknownProviderException("repo", provider.toString())
    }
  }
}
