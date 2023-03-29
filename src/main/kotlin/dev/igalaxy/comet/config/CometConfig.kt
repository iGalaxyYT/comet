package dev.igalaxy.comet.config

import com.google.gson.GsonBuilder
import dev.isxander.yacl.api.ConfigCategory
import dev.isxander.yacl.api.Option
import dev.isxander.yacl.api.OptionGroup
import dev.isxander.yacl.api.YetAnotherConfigLib
import dev.isxander.yacl.config.ConfigEntry
import dev.isxander.yacl.config.GsonConfigInstance
import dev.isxander.yacl.gui.controllers.TickBoxController
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import org.quiltmc.loader.api.QuiltLoader

class CometConfig {
    companion object {
        val INSTANCE: GsonConfigInstance<CometConfig> = GsonConfigInstance(CometConfig::class.java, QuiltLoader.getConfigDir().resolve("comet.json"), GsonBuilder::setPrettyPrinting)

        fun makeScreen(parent: Screen): Screen {
            return YetAnotherConfigLib.create(INSTANCE, fun(defaults: CometConfig, config: CometConfig, builder: YetAnotherConfigLib.Builder): YetAnotherConfigLib.Builder {
                val categoryBuilder = ConfigCategory.createBuilder()
                    .name(Text.translatable("comet.config.title"))

                val modulesGroup = OptionGroup.createBuilder()
                    .name(Text.translatable("comet.config.group.modules.title"))
                    .tooltip(Text.translatable("comet.config.group.modules.description"))

                val pronounsOption = Option.createBuilder(Boolean::class.java)
                    .name(Text.translatable("comet.config.option.pronouns"))
                    .tooltip(Text.translatable("comet.config.option.pronouns.description"))
                    .binding(
                        defaults.pronouns,
                        { config.pronouns },
                        { value: Boolean ->
                            config.pronouns = value
                        }
                    )
                    .controller(::TickBoxController)
                    .build()

                modulesGroup.option(pronounsOption)
                categoryBuilder.group(modulesGroup.build())

                return builder
                    .title(Text.translatable("comet.config.title"))
                    .category(categoryBuilder.build())
            }).generateScreen(parent)
        }
    }

    @ConfigEntry
    var pronouns: Boolean = true
}
