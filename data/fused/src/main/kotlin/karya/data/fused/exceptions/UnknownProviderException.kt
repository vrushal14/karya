package karya.data.fused.exceptions

import karya.core.exceptions.KaryaException

class UnknownProviderException(
	rootLevelEntity: String,
	providerPassed: String,
) : KaryaException("Unknown provider [$providerPassed] for entity : [$rootLevelEntity]")
