package karya.core.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import karya.core.exceptions.ConfigException.*
import java.io.File

inline fun <reified T> getSection(
  filePath: String,
  section: String,
): T {
  val mapper = ObjectMapper(YAMLFactory()).apply { findAndRegisterModules() }
  val yamlContent = File(filePath).readText()
  val yamlMap = mapper.readValue(yamlContent, Map::class.java) as Map<*, *>
  return yamlMap[section] as? T ?: throw YamlSectionNotFoundException(section)
}

inline fun <reified T> Map<*, *>.readValue(key: String): T {
  val value = this[key] ?: throw YamlMapKeyNotSetException(key)

  return when (T::class) {
    Long::class -> (value as? Number)?.toLong() as T
    Int::class -> (value as? Number)?.toInt() as T
    Double::class -> (value as? Number)?.toDouble() as T
    Float::class -> (value as? Number)?.toFloat() as T
    else -> value as? T ?: throw InvalidYamlMapKeyException(key, T::class.toString())
  }
}
