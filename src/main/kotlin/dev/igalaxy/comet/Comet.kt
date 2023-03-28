package dev.igalaxy.comet

import dev.igalaxy.comet.config.CometConfig
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Comet : ModInitializer {
    val LOGGER: Logger = LoggerFactory.getLogger("comet")

    override fun onInitialize(mod: ModContainer) {
        CometConfig.INSTANCE.load()
    }
}
