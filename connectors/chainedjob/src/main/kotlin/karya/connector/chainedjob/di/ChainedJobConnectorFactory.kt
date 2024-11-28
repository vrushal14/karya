package karya.connector.chainedjob.di

import karya.core.configs.RepoConfig
import karya.data.fused.di.factories.FusedRepoComponentFactory

object ChainedJobConnectorFactory {
  fun build(config: RepoConfig) =
    DaggerChainedJobConnectorComponent.builder()
      .fusedRepoDataComponent(FusedRepoComponentFactory.build(config))
      .build()
      .connector
}
