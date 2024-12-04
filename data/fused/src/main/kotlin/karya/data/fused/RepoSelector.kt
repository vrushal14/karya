package karya.data.fused

import karya.core.configs.RepoConfig
import karya.core.utils.getSection
import karya.core.utils.readValue
import karya.data.fused.exceptions.FusedDataException.UnknownProviderException
import karya.data.psql.configs.PsqlRepoConfig
import karya.data.psql.configs.PsqlRepoConfig.Companion.PSQL_IDENTIFIER

/**
 * Object responsible for selecting the appropriate repository configuration
 * based on the provided file path.
 */
object RepoSelector {

  /**
   * Retrieves the repository configuration from the specified file path.
   *
   * @param filePath The path to the configuration file.
   * @return The repository configuration.
   * @throws UnknownProviderException If the provider specified in the configuration is unknown.
   */
  fun get(filePath: String): RepoConfig {
    val section: Map<String, *> = getSection(filePath, "repo")
    val properties = section["properties"] as Map<*, *>

    return when (val provider = section["provider"]) {
      PSQL_IDENTIFIER -> PsqlRepoConfig(properties, section.readValue("partitions"))

      else -> throw UnknownProviderException("repo", provider.toString())
    }
  }
}
