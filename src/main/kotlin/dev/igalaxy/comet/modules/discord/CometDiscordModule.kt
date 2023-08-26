package dev.igalaxy.comet.modules.discord

import com.jagrosh.discordipc.IPCClient
import com.jagrosh.discordipc.entities.RichPresence
import com.jagrosh.discordipc.entities.pipe.PipeStatus
import dev.igalaxy.comet.Comet
import dev.igalaxy.comet.modules.CometModule
import net.minecraft.client.network.ServerInfo
import org.quiltmc.loader.api.QuiltLoader
import org.quiltmc.qsl.lifecycle.api.client.event.ClientLifecycleEvents

object CometDiscordModule : CometModule {
    override val enabled: Boolean
        get() = Comet.CONFIG.discordEnabled

    private val client = IPCClient(Comet.CONFIG.discordClient.toLong())
    private val startTimestamp = System.currentTimeMillis()

    init {
        client.setListener(CometDiscordIPCListener())

        checkClient()

        ClientLifecycleEvents.Stopping {
            if (client.status == PipeStatus.CONNECTED) {
                client.close()
            }
        }
    }

    fun checkClient() {
        if (enabled) {
            client.connect()
        } else {
            if (client.status == PipeStatus.CONNECTED) {
                client.close()
            }
        }
    }

    fun clientReady() {
        val minecraftVersion = QuiltLoader.getNormalizedGameVersion()
        val quiltVersion = QuiltLoader.getModContainer("quilt_loader").get().metadata().version().raw()

        val builder = RichPresence.Builder()
        builder
            .setStartTimestamp(startTimestamp)
            .setLargeImage("grass_block", "Minecraft $minecraftVersion")
            .setSmallImage("quilt", "Quilt Loader $quiltVersion")
        client.sendRichPresence(builder.build())
    }

    fun updateServer(serverInfo: ServerInfo) {
        val minecraftVersion = QuiltLoader.getNormalizedGameVersion()
        val quiltVersion = QuiltLoader.getModContainer("quilt_loader").get().metadata().version().raw()

        var name = serverInfo.name
        name = name.replace(Regex("/§[0-9A-FK-OR]/ig"), "")

        val builder = RichPresence.Builder()
        builder
            .setStartTimestamp(startTimestamp)
            .setLargeImage("https://api.mcsrvstat.us/icon/" + serverInfo.address, "Minecraft $minecraftVersion")
            .setSmallImage("quilt", "Quilt Loader $quiltVersion")
            .setState("Multiplayer")
            .setDetails(name)
        client.sendRichPresence(builder.build())
    }
}
