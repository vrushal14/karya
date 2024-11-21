package karya.data.fused.di.modules

import dagger.Module
import dagger.Provides
import karya.core.configs.LocksConfig
import karya.data.fused.exceptions.UnknownProviderException
import karya.data.redis.configs.RedisLocksConfig
import karya.data.redis.di.RedisLocksClientFactory
import javax.inject.Singleton

@Module
class FusedLocksModule {

  @Provides
  @Singleton
  fun provideLocksClient(locksConfig: LocksConfig) = when(locksConfig) {
    is RedisLocksConfig -> RedisLocksClientFactory.build(locksConfig)

    else -> throw UnknownProviderException("lock", locksConfig.provider)
  }
}