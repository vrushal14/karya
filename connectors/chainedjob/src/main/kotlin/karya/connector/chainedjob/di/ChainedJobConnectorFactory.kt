package karya.connector.chainedjob.di

import karya.core.configs.RepoConfig
import karya.data.fused.di.factories.FusedDataRepoComponentFactory

object ChainedJobConnectorFactory {
  fun build(config: RepoConfig) =
    DaggerChainedJobConnectorComponent.builder()
      .fusedDataRepoComponent(FusedDataRepoComponentFactory.build(config))
      .build()
      .connector
}
