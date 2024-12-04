package karya.core.usecases

import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset

/**
 * Calculates the next execution time based on the given start time and period.
 *
 * @param startTime The initial start time.
 * @param periodTime The period duration in ISO-8601 format.
 * @return The next execution time in milliseconds since the epoch.
 */
fun getNextExecutionAt(startTime: Instant, periodTime: String) =
  startTime
    .plus(Duration.parse(periodTime))
    .atZone(ZoneOffset.UTC)
    .toInstant()
    .toEpochMilli()
