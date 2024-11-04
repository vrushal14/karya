package karya.data.redis.di

import dagger.Binds
import dagger.Module
import karya.core.locks.LocksClient
import karya.data.redis.RedisLocksClient
import javax.inject.Singleton

@Module
abstract class RedisModule {

  @Binds
  @Singleton
  abstract fun provideLocksClient(redisLocksRepo: RedisLocksClient) : LocksClient
}