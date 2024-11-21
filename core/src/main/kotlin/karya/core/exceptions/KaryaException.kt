package karya.core.exceptions

open class KaryaException(
	override val message: String,
) : Exception(message)
