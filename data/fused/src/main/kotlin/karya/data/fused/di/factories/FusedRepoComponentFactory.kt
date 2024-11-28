package karya.data.fused.di.factories

import karya.core.configs.RepoConfig
import karya.data.fused.di.components.DaggerFusedRepoDataComponent

object FusedRepoComponentFactory {
  fun build(repoConfig: RepoConfig) =
    DaggerFusedRepoDataComponent.builder()
      .repoConfig(repoConfig)
      .build()
}
