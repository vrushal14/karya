package karya.data.redis

import karya.core.locks.LocksClient
import karya.core.locks.entities.LockResult
import karya.data.redis.configs.RedisLocksConfig
import org.apache.logging.log4j.kotlin.Logging
import org.apache.logging.log4j.kotlin.logger
import org.redisson.api.RedissonClient
import java.util.*
import javax.inject.Inject

class RedisLocksClient
@Inject
constructor(
  private val redissonClient: RedissonClient,
  private val config: RedisLocksConfig,
) : LocksClient {
  companion object : Logging

  override suspend fun <T> withLock(
    id: UUID,
    block: suspend () -> T,
  ): LockResult<T> =
    if (getLock(id)) {
      val res: T
      try {
        res = block()
      } finally {
        freeLock(id)
      }
      LockResult.Success(res)
    } else {
      LockResult.Failure
    }

  override suspend fun shutdown(): Boolean {
    try {
      redissonClient.shutdown()
      logger.info("RedisLocksClient successfully shutdown")
      return true
    } catch (e: Exception) {
      logger.error("Error shutting down RedisLocksClient --- $e")
      return false
    }
  }

  private fun getLock(id: UUID): Boolean {
    val lock = redissonClient.getLock(id.toString())
    return lock
      .tryLock(config.waitTime, config.leaseTime, java.util.concurrent.TimeUnit.MILLISECONDS)
      .also {
        if (it) {
          logger.debug(
            "Acquired lock --- $id",
          )
        } else {
          logger.warn("Failed to acquire lock --- $id")
        }
      }
  }

  private fun freeLock(id: UUID) {
    val lock = redissonClient.getLock(id.toString())
    if (lock.isHeldByCurrentThread) lock.unlock()
    logger.debug("Released lock --- $id")
  }
}
