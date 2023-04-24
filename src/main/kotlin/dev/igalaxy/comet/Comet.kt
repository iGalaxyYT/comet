package dev.igalaxy.comet

import dev.igalaxy.comet.config.CometConfig
import dev.igalaxy.comet.modules.tooltip.CometTooltipModule
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Comet : ModInitializer {
    val LOGGER: Logger = LoggerFactory.getLogger("comet")
    val CONFIG: CometConfig
        get() = CometConfig.INSTANCE.config

    override fun onInitialize(mod: ModContainer) {
        CometConfig.INSTANCE.load()

        CometTooltipModule
    }
}
