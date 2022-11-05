package am.panov.vk

import derevo.derive
import derevo.pureconfig.pureconfigReader
import pureconfig.ConfigSource

@derive(pureconfigReader)
final case class VKConfig(host: String, accessToken: String, version: String)

object VKConfig {
  def load: VKConfig = ConfigSource.default.at("am.panov.vk").loadOrThrow[VKConfig]
}
