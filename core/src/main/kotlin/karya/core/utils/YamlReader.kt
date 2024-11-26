package karya.core.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import karya.core.exceptions.KaryaException
import java.io.File

inline fun <reified T> getSection(
  filePath: String,
  section: String,
): T {
  val mapper = ObjectMapper(YAMLFactory()).apply { findAndRegisterModules() }
  val yamlContent = File(filePath).readText()
  val yamlMap = mapper.readValue(yamlContent, Map::class.java) as Map<*, *>
  return yamlMap[section] as? T ?: throw KaryaException("Section $section not found in YAML file!")
}

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
