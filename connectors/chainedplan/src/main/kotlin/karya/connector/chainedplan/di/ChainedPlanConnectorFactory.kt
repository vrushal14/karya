package karya.connector.chainedplan.di

import karya.data.fused.di.components.FusedDataRepoComponent

object ChainedPlanConnectorFactory {
  fun build(fusedDataRepoComponent: FusedDataRepoComponent) =
    DaggerChainedPlanConnectorComponent.builder()
      .fusedDataRepoComponent(fusedDataRepoComponent)
      .build()
      .connector
}
