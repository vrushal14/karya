package karya.connector.chainedplan.di

import karya.data.fused.di.components.FusedDataRepoComponent

/**
 * Factory object for creating instances of ChainedPlanConnector.
 */
object ChainedPlanConnectorFactory {

  /**
   * Builds a ChainedPlanConnector using the provided FusedDataRepoComponent.
   *
   * @param fusedDataRepoComponent The component for fused data repository.
   * @return An instance of ChainedPlanConnector.
   */
  fun build(fusedDataRepoComponent: FusedDataRepoComponent) =
    DaggerChainedPlanConnectorComponent.builder()
      .fusedDataRepoComponent(fusedDataRepoComponent)
      .build()
      .connector
}
