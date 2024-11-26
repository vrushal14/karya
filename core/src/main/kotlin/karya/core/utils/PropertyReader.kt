package karya.core.utils

import java.util.*

object PropertyReader {

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
