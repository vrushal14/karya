package karya.connectors.chained

import karya.connectors.chained.di.DaggerChainedConnectorComponent
import karya.data.fused.di.components.FusedDataRepoComponent

/**
 * Factory object for creating instances of ChainedConnector.
 */
object ChainedConnectorFactory {

  /**
   * Builds a ChainedConnector using the provided FusedDataRepoComponent.
   *
   * @param fusedDataRepoComponent The component for fused data repository.
   * @return An instance of ChainedPlanConnector.
   */
  fun build(fusedDataRepoComponent: FusedDataRepoComponent) =
    DaggerChainedConnectorComponent.builder()
      .fusedDataRepoComponent(fusedDataRepoComponent)
      .build()
      .connector
}
