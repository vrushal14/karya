package karya.data.redis

import karya.core.locks.LocksClient
import karya.data.redis.configs.RedisLocksConfig
import kotlinx.coroutines.coroutineScope
import org.apache.logging.log4j.kotlin.Logging
import org.redisson.api.RedissonClient
import java.util.*
import javax.inject.Inject

class RedisLocksClient
@Inject
constructor(
  private val redissonClient : RedissonClient,
  private val config: RedisLocksConfig
) : LocksClient {

  companion object : Logging

  override suspend fun getLock(id: UUID): Boolean = coroutineScope {
    val lock = redissonClient.getLock(id.toString())
    return@coroutineScope lock.tryLock(config.waitTime, config.leaseTime, java.util.concurrent.TimeUnit.MILLISECONDS)
  }.also { if (it) logger.debug("Acquired lock --- $id") else logger.warn("Failed to acquire lock --- $id") }

  override suspend fun freeLock(id: UUID) {
    val lock = redissonClient.getLock(id.toString())
    if (lock.isHeldByCurrentThread) {
      lock.unlock() // Release the lock
    }
    logger.debug("Released lock --- $id")
  }
}