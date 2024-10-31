package karya.core.usecases

import java.util.UUID

// to be used when stickiness is preferred
fun createPartitionKey(id : UUID, partitions : Int) =
  (id.hashCode() % partitions)
    .let { if (it < 0) it + partitions else it }
    .let { if (it == 0) partitions else it }

// to be used when randomness is preferred
fun createPartitionKey(partitions : Int) =
  (UUID.randomUUID().hashCode() % partitions)
    .let { if (it < 0) it + partitions else it }
    .let { if (it == 0) partitions else it }