package karya.data.redis.di

import dagger.Binds
import dagger.Module
import karya.core.connectors.LockConnector
import karya.core.locks.LocksClient
import karya.data.redis.RedisLockConnector
import karya.data.redis.RedisLocksClient
import javax.inject.Singleton

@Module
abstract class RedisModule {

  @Binds
  @Singleton
  abstract fun provideLocksClient(redisLocksRepo: RedisLocksClient) : LocksClient

  @Binds
  @Singleton
  abstract fun provideRedisLockConnector(redisLockConnector: RedisLockConnector) : LockConnector
}