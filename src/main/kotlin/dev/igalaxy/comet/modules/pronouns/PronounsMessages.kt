package dev.igalaxy.comet.modules.pronouns

import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text
import org.quiltmc.qkl.library.text.*

object PronounsMessages {
    private val client = MinecraftClient.getInstance()

    fun getChatMessageWithPronouns(message: Text, pronouns: Pronouns): Text {
        if (pronouns == Pronouns.UNSPECIFIED)
            return message

        return buildText {
            color(Color.GREY) {
                pronouns.getText()
            }
            text(message)
        }
    }

    fun getTextWithColoredPronouns(name: Text, pronouns: Pronouns): Text {
        if (pronouns == Pronouns.UNSPECIFIED)
            return name

        return buildText {
            text(name)
            color(Color.GREY) {
                literal(" [")
                text(pronouns.getText())
                literal("]")
            }
        }
    }
}
