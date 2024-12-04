package karya.data.fused.di.factories

import karya.core.configs.LocksConfig
import karya.data.fused.di.components.DaggerFusedDataLocksComponent

/**
 * Factory object for creating instances of FusedDataLocksComponent.
 */
object FusedDataLocksComponentFactory {

  /**
   * Builds a FusedDataLocksComponent using the provided locks configuration.
   *
   * @param locksConfig The configuration for locks.
   * @return An instance of FusedDataLocksComponent.
   */
  fun build(locksConfig: LocksConfig) =
    DaggerFusedDataLocksComponent.builder()
      .locksConfig(locksConfig)
      .build()
}
