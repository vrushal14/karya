package karya.servers.server.configs

import karya.core.configs.LocksConfig
import karya.core.configs.RepoConfig
import karya.core.utils.getSection
import karya.data.fused.LocksSelector
import karya.data.fused.RepoSelector

data class ServerConfig(
  val strictMode: Boolean,
  val maxChainedDepth: Int,

  val repoConfig: RepoConfig,
  val locksConfig: LocksConfig
) {

  companion object {
    fun load(providersFilePath: String, serverConfigFilePath: String): ServerConfig {
      val section: Map<String, *> = getSection(serverConfigFilePath, "application")

      return ServerConfig(
        strictMode = section["strictMode"] as Boolean,
        maxChainedDepth = section["maxChainedDepth"] as Int,
        repoConfig = RepoSelector.get(providersFilePath),
        locksConfig = LocksSelector.get(providersFilePath)
      )
    }
  }

}
