package karya.data.redis.di

import karya.data.redis.configs.RedisLocksConfig

object RedisLocksClientFactory {
  fun build(config: RedisLocksConfig) =
    DaggerRedisComponent.builder()
      .redisLocksConfig(config)
      .build()
      .locksClient
}