package karya.core.utils

import java.util.*

fun Properties.loadFile(path: String): Properties = this.apply {
  val inputStream = Thread.currentThread().contextClassLoader.getResourceAsStream(path)
  inputStream.use { load(it) }
}