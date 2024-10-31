package karya.core.utils

import java.nio.file.Paths

fun getConfigPath(moduleName: String) : String =
  System.getenv("CONFIG_PATH") ?: Paths.get("").toAbsolutePath()
      .resolve("$moduleName/src/main/resources/manifest.yaml").toString()