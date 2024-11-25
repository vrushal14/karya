package karya.core.configs

abstract class RepoConfig(
    val provider: String,
    open val partitions: Int,
)
