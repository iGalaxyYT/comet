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

                val tweaksGroup = OptionGroup.createBuilder()
                    .name(Text.translatable("comet.config.group.tweaks.title"))
                    .tooltip(Text.translatable("comet.config.group.tweaks.description"))

                val tooltipModule = Option.createBuilder(Boolean::class.java)
                    .name(Text.translatable("comet.config.option.tooltipModule"))
                    .tooltip(Text.translatable("comet.config.option.tooltipModule.description"))
                    .binding(
                        defaults.tooltipModule,
                        { config.tooltipModule },
                        { value: Boolean ->
                            config.tooltipModule = value
                        }
                    )
                    .controller(::TickBoxController)
                    .build()

                val serverNameLengthTweak = Option.createBuilder(Boolean::class.java)
                    .name(Text.translatable("comet.config.option.serverNameLengthTweak"))
                    .tooltip(Text.translatable("comet.config.option.serverNameLengthTweak.description"))
                    .binding(
                        defaults.serverNameLengthTweak,
                        { config.serverNameLengthTweak },
                        { value: Boolean ->
                            config.serverNameLengthTweak = value
                        }
                    )
                    .controller(::TickBoxController)
                    .build()

                modulesGroup.option(tooltipModule)
                tweaksGroup.option(serverNameLengthTweak)
                categoryBuilder.group(modulesGroup.build())
                categoryBuilder.group(tweaksGroup.build())

                return builder
                    .title(Text.translatable("comet.config.title"))
                    .category(categoryBuilder.build())
            }).generateScreen(parent)
        }
    }

    @ConfigEntry
    var tooltipModule: Boolean = true

    @ConfigEntry
    var serverNameLengthTweak: Boolean = true
}
