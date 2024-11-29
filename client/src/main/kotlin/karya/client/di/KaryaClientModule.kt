package karya.client.di

import dagger.Binds
import dagger.Module
import karya.client.ktor.KaryaClientImpl
import karya.core.actors.Client
import javax.inject.Singleton

@Module
abstract class KaryaClientModule {

  @Binds
  @Singleton
  abstract fun provideKaryaClient(client: KaryaClientImpl): Client
}
