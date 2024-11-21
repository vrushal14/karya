package karya.data.redis.di

import karya.data.redis.configs.RedisLocksConfig

object RedisLocksClientFactory {
  fun build(config: RedisLocksConfig) =
    DaggerRedisLocksComponent.builder()
      .redisLocksConfig(config)
      .build()
      .locksClient
}