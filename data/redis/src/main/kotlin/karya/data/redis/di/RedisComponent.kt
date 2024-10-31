package karya.data.redis.di

import dagger.BindsInstance
import dagger.Component
import karya.core.connectors.LockConnector
import karya.core.locks.LocksClient
import karya.data.redis.configs.RedisLocksConfig
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    RedisModule::class,
    RedisUtilsModule::class
  ]
)
interface RedisComponent {

  val locksClient : LocksClient
  val lockConnector : LockConnector

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun redisLocksConfig(redisLocksConfig: RedisLocksConfig) : Builder

    fun build() : RedisComponent

  }
}