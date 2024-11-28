package karya.data.fused.di.factories

import karya.core.configs.RepoConfig
import karya.data.fused.di.components.DaggerFusedDataRepoComponent

object FusedDataRepoComponentFactory {
  fun build(repoConfig: RepoConfig) =
    DaggerFusedDataRepoComponent.builder()
      .repoConfig(repoConfig)
      .build()
}
