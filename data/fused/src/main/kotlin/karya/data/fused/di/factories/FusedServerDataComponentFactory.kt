package karya.data.fused.di.factories

import karya.core.configs.LocksConfig
import karya.core.configs.RepoConfig
import karya.data.fused.di.components.DaggerFusedServerDataComponent

object FusedServerDataComponentFactory {
  fun build(repoConfig: RepoConfig, locksConfig: LocksConfig) =
    DaggerFusedServerDataComponent.builder()
      .repoConfig(repoConfig)
      .locksConfig(locksConfig)
      .build()
}
