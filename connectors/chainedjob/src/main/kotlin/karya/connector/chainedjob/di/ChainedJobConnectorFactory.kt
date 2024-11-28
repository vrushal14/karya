package karya.connector.chainedjob.di

import karya.data.fused.di.components.FusedDataRepoComponent

object ChainedJobConnectorFactory {
  fun build(fusedDataRepoComponent: FusedDataRepoComponent) =
    DaggerChainedJobConnectorComponent.builder()
      .fusedDataRepoComponent(fusedDataRepoComponent)
      .build()
      .connector
}
