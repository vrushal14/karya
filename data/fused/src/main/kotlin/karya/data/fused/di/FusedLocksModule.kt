package karya.data.fused.di

import dagger.Module
import dagger.Provides
import karya.core.connectors.LockConnector
import karya.core.locks.LocksClient
import karya.data.fused.locks.LocksConfig
import karya.data.fused.locks.LocksWrapper
import karya.data.redis.di.DaggerRedisComponent
import javax.inject.Singleton

@Module
class FusedLocksModule {

  @Provides
  @Singleton
  fun provideLockConnector(wrapper: LocksWrapper) : LockConnector = wrapper.locksConnector

  @Provides
  @Singleton
  fun provideLocksClient(wrapper: LocksWrapper) : LocksClient = wrapper.locksClient

  @Provides
  @Singleton
  fun provideLocksWrapper(locksConfig: LocksConfig) : LocksWrapper = when(locksConfig) {
    is LocksConfig.Redis -> provideRedisLocksWrapper(locksConfig)
  }

  private fun provideRedisLocksWrapper(redis: LocksConfig.Redis) : LocksWrapper {
    val component = DaggerRedisComponent.builder()
      .redisLocksConfig(redis.redisLocksConfig)
      .build()

    return LocksWrapper(
      locksClient = component.locksClient,
      locksConnector = component.lockConnector
    )
  }
}