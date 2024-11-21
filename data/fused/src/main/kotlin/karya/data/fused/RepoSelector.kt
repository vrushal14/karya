package karya.data.fused

import karya.core.configs.RepoConfig
import karya.core.utils.PropsReader.readValue
import karya.data.fused.exceptions.UnknownProviderException
import karya.data.fused.utils.getSection
import karya.data.psql.configs.PsqlRepoConfig

object RepoSelector {

  private const val PSQL = "psql"

  fun get(filePath : String) : RepoConfig {
    val section = getSection(filePath, "repo")
    val properties = section["properties"] as Map<*,*>
    return when(val provider = section["provider"]) {
      PSQL -> PsqlRepoConfig(properties, section.readValue("partitions"))

      else -> throw UnknownProviderException("repo", provider.toString())
    }
  }
}