package karya.core.usecases

import java.util.*

/**
 * Creates a partition key based on the given UUID and number of partitions.
 * This method is used when stickiness is preferred.
 *
 * @param id The UUID to be used for generating the partition key.
 * @param partitions The number of partitions.
 * @return The generated partition key.
 */
fun createPartitionKey(id: UUID, partitions: Int): Int =
  (id.hashCode() % partitions)
    .let { if (it < 0) it + partitions else it }
    .let { if (it == 0) partitions else it }

/**
 * Creates a partition key based on a random UUID and number of partitions.
 * This method is used when randomness is preferred.
 *
 * @param partitions The number of partitions.
 * @return The generated partition key.
 */
fun createPartitionKey(partitions: Int): Int =
  (UUID.randomUUID().hashCode() % partitions)
    .let { if (it < 0) it + partitions else it }
    .let { if (it == 0) partitions else it }
