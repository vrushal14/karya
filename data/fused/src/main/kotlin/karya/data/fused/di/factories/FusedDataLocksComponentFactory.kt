package karya.data.fused.di.factories

import karya.core.configs.LocksConfig
import karya.data.fused.di.components.DaggerFusedDataLocksComponent

object FusedDataLocksComponentFactory {
  fun build(locksConfig: LocksConfig) =
    DaggerFusedDataLocksComponent.builder()
      .locksConfig(locksConfig)
      .build()
}
