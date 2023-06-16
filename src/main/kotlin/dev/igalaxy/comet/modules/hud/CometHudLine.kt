package dev.igalaxy.comet.modules.hud

import dev.isxander.yacl3.api.NameableEnum
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text
import kotlin.math.floor

enum class CometHudLine : NameableEnum {
    FPS {
        override fun getText(): Text? {
            return Text.of("${MinecraftClient.getInstance().currentFps} fps")
        }
    }, PLAYTIME {
        override fun getText(): Text? {
            if (CometHudModule.showExistencePlaytime) {
                val playtimeMinutes = floor((CometHudModule.playtimeSeconds / 60).toDouble()).toInt()
                val correctPlaytimeSeconds = CometHudModule.playtimeSeconds % 60

                return Text.of("${CometHudModule.playtimeHours}h ${playtimeMinutes}m ${correctPlaytimeSeconds}s")
            }
            return null
        }
    };

    override fun getDisplayName(): Text {
        return Text.translatable("comet.config.modules.hud." + name.lowercase())
    }

    abstract fun getText(): Text?
}
