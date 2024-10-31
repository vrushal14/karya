package karya.data.redis.di

import dagger.Module
import dagger.Provides
import karya.data.redis.configs.RedisLocksConfig
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import javax.inject.Singleton

@Module
class RedisUtilsModule {

  @Provides
  @Singleton
  fun provideRedissonClient(redisLocksConfig: RedisLocksConfig) : RedissonClient {
    val config = Config()
    config.useSingleServer().setAddress(redisLocksConfig.url)
    return Redisson.create(config)
  }

}