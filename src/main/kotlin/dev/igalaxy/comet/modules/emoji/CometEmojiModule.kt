package dev.igalaxy.comet.modules.emoji

import com.vdurmont.emoji.EmojiManager
import dev.igalaxy.comet.Comet
import dev.igalaxy.comet.modules.CometModule

object CometEmojiModule : CometModule {
    override val enabled: Boolean
        get() = Comet.CONFIG.emojiEnabled

    fun getSuggestions(currentWord: String): Collection<String> {
        if (currentWord.startsWith(":")) {
            val tags = mutableListOf<String>()
            EmojiManager.getAll().forEach {
                it.aliases.forEach { alias ->
                    tags.add(":${alias}:")
                }
            }
            return tags.filter { it.startsWith(currentWord) }
        }
        return listOf()
    }
}
