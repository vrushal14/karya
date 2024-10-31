package karya.data.redis

import karya.core.connectors.LockConnector
import org.apache.logging.log4j.kotlin.logger
import org.redisson.api.RedissonClient
import javax.inject.Inject

class RedisLockConnector
@Inject
constructor(
  private val redissonClient: RedissonClient
) : LockConnector {

  override suspend fun shutdown() : Boolean {
    try {
      redissonClient.shutdown()
      return true
    } catch (e : Exception) {
      logger.error(e)
      return false
    }
  }
}