package karya.data.fused.di

import dagger.Module
import dagger.Provides
import karya.core.configs.LocksConfig
import karya.data.fused.exceptions.UnknownProviderException
import karya.data.redis.configs.RedisLocksConfig
import karya.data.redis.di.DaggerRedisComponent
import javax.inject.Singleton

@Module
class FusedLocksModule {

  @Provides
  @Singleton
  fun provideLocksClient(locksConfig: LocksConfig) = when(locksConfig) {
    is RedisLocksConfig -> provideRedisLocksClient(locksConfig)

    else -> throw UnknownProviderException("lock", locksConfig.provider)
  }

  private fun provideRedisLocksClient(config: RedisLocksConfig) =
    DaggerRedisComponent.builder()
      .redisLocksConfig(config)
      .build()
      .locksClient
}