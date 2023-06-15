package dev.igalaxy.comet.modules.discord

import com.jagrosh.discordipc.IPCClient
import com.jagrosh.discordipc.entities.pipe.PipeStatus
import dev.igalaxy.comet.Comet
import dev.igalaxy.comet.modules.CometModule
import org.quiltmc.qsl.lifecycle.api.client.event.ClientLifecycleEvents

object CometDiscordModule : CometModule {
    override val enabled: Boolean
        get() = Comet.CONFIG.discordEnabled

    private val client = IPCClient(Comet.CONFIG.discordClient.toLong())

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
}
