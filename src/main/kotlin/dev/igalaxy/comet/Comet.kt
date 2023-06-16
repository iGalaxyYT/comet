package dev.igalaxy.comet

import dev.igalaxy.comet.config.CometConfig
import dev.igalaxy.comet.modules.discord.CometDiscordModule
import dev.igalaxy.comet.modules.tooltip.CometTooltipModule
import net.minecraft.util.Identifier
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.lifecycle.api.client.event.ClientLifecycleEvents
import org.quiltmc.qsl.lifecycle.api.client.event.ClientWorldTickEvents
import org.quiltmc.qsl.resource.loader.api.ResourceLoader
import org.quiltmc.qsl.resource.loader.api.ResourcePackActivationType
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Comet : ModInitializer {
    val LOGGER: Logger = LoggerFactory.getLogger("comet")
    val CONFIG: CometConfig
        get() = CometConfig.INSTANCE.config

    override fun onInitialize(mod: ModContainer) {
        CometConfig.INSTANCE.load()

        ResourceLoader.registerBuiltinResourcePack(Identifier(mod.metadata().id(), "pee_mod"), mod, ResourcePackActivationType.NORMAL)

        CometTooltipModule
        CometDiscordModule
    }
}
