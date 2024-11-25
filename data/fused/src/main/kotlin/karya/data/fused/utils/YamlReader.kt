package karya.data.fused.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import karya.core.exceptions.KaryaException
import java.io.File

fun getSection(
    filePath: String,
    section: String,
): Map<*, *> {
    val mapper = ObjectMapper(YAMLFactory()).apply { findAndRegisterModules() }
    val yamlContent = File(filePath).readText()
    val yamlMap = mapper.readValue(yamlContent, Map::class.java) as Map<*, *>
    return yamlMap[section] as? Map<*, *> ?: throw KaryaException("Section $section not found in YAML file!")
}
