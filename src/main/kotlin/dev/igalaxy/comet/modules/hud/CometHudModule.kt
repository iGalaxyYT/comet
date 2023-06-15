package dev.igalaxy.comet.modules.hud

import dev.igalaxy.comet.Comet
import dev.igalaxy.comet.modules.CometModule
import net.minecraft.client.MinecraftClient

object CometHudModule : CometModule {
    override val enabled: Boolean
        get() = Comet.CONFIG.hudEnabled

    fun getHudText(): String {
        val builder = StringBuilder("")

        if (Comet.CONFIG.hudFps) {
            builder.append("${MinecraftClient.getInstance().currentFps} fps")
        }

        return builder.toString()
    }
}
