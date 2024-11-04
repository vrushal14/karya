package karya.data.fused.di

import dagger.Module
import dagger.Provides
import karya.data.fused.locks.LocksConfig
import karya.data.redis.di.DaggerRedisComponent
import javax.inject.Singleton

@Module
class FusedLocksModule {

  @Provides
  @Singleton
  fun provideLocksClient(locksConfig: LocksConfig) = when(locksConfig) {
    is LocksConfig.Redis -> provideRedisLocksClient(locksConfig)
  }

  private fun provideRedisLocksClient(config: LocksConfig.Redis) =
    DaggerRedisComponent.builder()
      .redisLocksConfig(config.redisLocksConfig)
      .build()
      .locksClient
}