package karya.data.fused.di.components

import dagger.BindsInstance
import dagger.Component
import karya.core.configs.LocksConfig
import karya.core.locks.LocksClient
import karya.data.fused.di.modules.FusedLocksModule
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    FusedLocksModule::class,
  ],
)
interface FusedDataLocksComponent {

  val locksClient: LocksClient

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun locksConfig(locksConfig: LocksConfig): Builder

    fun build(): FusedDataLocksComponent
  }
}
