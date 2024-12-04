package karya.core.configs

/**
 * Abstract class representing the configuration for repositories.
 *
 * @property provider This helps reference what interface is being used to provide the repo.
 * @property partitions The number of partitions to be created for the repository.
 */
abstract class RepoConfig(
  val provider: String,
  open val partitions: Int,
)
