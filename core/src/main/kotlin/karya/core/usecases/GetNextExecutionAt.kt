package karya.core.usecases

import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset

fun getNextExecutionAt(
    startTime: Instant,
    periodTime: String,
) = startTime
    .plus(Duration.parse(periodTime))
    .atZone(ZoneOffset.UTC)
    .toInstant()
    .toEpochMilli()
