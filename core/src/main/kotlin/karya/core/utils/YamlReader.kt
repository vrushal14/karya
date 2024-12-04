package karya.core.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import karya.core.exceptions.ConfigException.*
import java.io.File
import java.util.*

/**
 * Reads a specific section from a YAML file and converts it to the specified type.
 *
 * @param T The type to which the section should be converted.
 * @param filePath The path to the YAML file.
 * @param section The section in the YAML file to be read.
 * @return The section converted to the specified type.
 * @throws YamlSectionNotFoundException if the section is not found in the YAML file.
 */
inline fun <reified T> getSection(filePath: String, section: String): T {
  val mapper = ObjectMapper(YAMLFactory()).apply { findAndRegisterModules() }
  val yamlContent = File(filePath).readText()
  val yamlMap = mapper.readValue(yamlContent, Map::class.java) as Map<*, *>
  return yamlMap[section] as? T ?: throw YamlSectionNotFoundException(section)
}

/**
 * Reads a value from a map and converts it to the specified type.
 *
 * @param T The type to which the value should be converted.
 * @param key The key whose value is to be read from the map.
 * @return The value converted to the specified type.
 * @throws YamlMapKeyNotSetException if the key is not set in the map.
 * @throws InvalidYamlMapKeyException if the value cannot be converted to the specified type.
 */
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

fun extractProperties(repo: Map<*, *>, key: String): Properties {
  val propertiesMap = (repo[key] as? Map<*, *>) ?: throw YamlMapKeyNotSetException(key)
  return Properties().apply {
    propertiesMap.forEach { (k, v) -> setProperty(k.toString(), v.toString()) }
  }
}
