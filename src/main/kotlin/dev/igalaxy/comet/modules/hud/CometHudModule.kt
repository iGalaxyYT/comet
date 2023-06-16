package dev.igalaxy.comet.modules.hud

import dev.igalaxy.comet.Comet
import dev.igalaxy.comet.modules.CometModule
import net.minecraft.client.MinecraftClient
import net.minecraft.stat.Stat
import net.minecraft.stat.StatType
import net.minecraft.stat.Stats
import kotlin.math.floor

object CometHudModule : CometModule {
    override val enabled: Boolean
        get() = Comet.CONFIG.hudEnabled

    var playtimeHours: Int = -1
    var playtimeSeconds: Int = -1
    var playtimeCheck: Long = -1

    val showExistencePlaytime: Boolean
        get() = System.currentTimeMillis() - playtimeCheck <= 2000

}
