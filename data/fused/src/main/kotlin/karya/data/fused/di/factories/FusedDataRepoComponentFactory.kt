package karya.data.fused.di.factories

import karya.core.configs.RepoConfig
import karya.data.fused.di.components.DaggerFusedDataRepoComponent

/**
 * Factory object for creating instances of FusedDataRepoComponent.
 */
object FusedDataRepoComponentFactory {

  /**
   * Builds a FusedDataRepoComponent using the provided repository configuration.
   *
   * @param repoConfig The configuration for the repository.
   * @return An instance of FusedDataRepoComponent.
   */
  fun build(repoConfig: RepoConfig) =
    DaggerFusedDataRepoComponent.builder()
      .repoConfig(repoConfig)
      .build()
}
