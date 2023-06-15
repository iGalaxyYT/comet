package dev.igalaxy.comet.modules.discord

import com.google.gson.JsonObject
import com.jagrosh.discordipc.IPCClient
import com.jagrosh.discordipc.IPCListener
import com.jagrosh.discordipc.entities.Packet
import com.jagrosh.discordipc.entities.RichPresence
import com.jagrosh.discordipc.entities.User
import org.quiltmc.loader.api.QuiltLoader

class CometDiscordIPCListener : IPCListener {
    override fun onPacketSent(client: IPCClient?, packet: Packet?) {

    }

    override fun onPacketReceived(client: IPCClient?, packet: Packet?) {

    }

    override fun onActivityJoin(client: IPCClient?, secret: String?) {

    }

    override fun onActivitySpectate(client: IPCClient?, secret: String?) {

    }

    override fun onActivityJoinRequest(client: IPCClient?, secret: String?, user: User?) {

    }

    override fun onReady(client: IPCClient?) {
        val minecraftVersion = QuiltLoader.getNormalizedGameVersion()
        val quiltVersion = QuiltLoader.getModContainer("quilt_loader").get().metadata().version().raw()

        val builder = RichPresence.Builder()
        builder
            .setStartTimestamp(System.currentTimeMillis())
            .setLargeImage("grass_block", "Minecraft $minecraftVersion")
            .setSmallImage("quilt", "Quilt Loader $quiltVersion")
        client?.sendRichPresence(builder.build())
    }

    override fun onClose(client: IPCClient?, json: JsonObject?) {

    }

    override fun onDisconnect(client: IPCClient?, t: Throwable?) {

    }
}
