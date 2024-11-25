package karya.core.utils

import java.util.*

object PropsReader {
    inline fun <reified T> Map<*, *>.readValue(key: String): T {
        val value = this[key] ?: throw IllegalArgumentException("$key value is not set!")

        return when (T::class) {
            Long::class -> (value as? Number)?.toLong() as T
            Int::class -> (value as? Number)?.toInt() as T
            Double::class -> (value as? Number)?.toDouble() as T
            Float::class -> (value as? Number)?.toFloat() as T
            else -> value as? T ?: throw IllegalArgumentException("Invalid type for key '$key'")
        }
    }

    inline fun <reified T> Properties.getKey(key: String): T {
        val value = this.getProperty(key) ?: throw IllegalArgumentException("$key value is not set!")

        return when (T::class) {
            Long::class -> value.toLong()
            Int::class -> value.toInt()
            Double::class -> value.toDouble()
            Float::class -> value.toFloat()
            else -> value as? T ?: throw IllegalArgumentException("Invalid type for key '$key'")
        } as T
    }
}
