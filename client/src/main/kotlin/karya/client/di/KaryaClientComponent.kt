package karya.client.di

import dagger.BindsInstance
import dagger.Component
import karya.client.configs.KaryaClientConfig
import karya.core.actors.Client
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    KaryaClientModule::class,
    KaryaClientUtilsModule::class,
  ],
)
interface KaryaClientComponent {
  val client: Client

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun clientConfig(config: KaryaClientConfig): Builder

    fun build(): KaryaClientComponent
  }
}
