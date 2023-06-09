package dev.igalaxy.comet.config

import com.google.gson.GsonBuilder
import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import dev.isxander.yacl3.config.ConfigEntry
import dev.isxander.yacl3.config.GsonConfigInstance
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import org.quiltmc.loader.api.QuiltLoader

class CometConfig {
    companion object {
        val INSTANCE: GsonConfigInstance<CometConfig> = GsonConfigInstance.createBuilder(CometConfig::class.java)
            .setPath(QuiltLoader.getConfigDir().resolve("comet.json"))
            .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
            .build()

        fun makeScreen(parent: Screen): Screen {
            return YetAnotherConfigLib.create(INSTANCE, fun(defaults: CometConfig, config: CometConfig, builder: YetAnotherConfigLib.Builder): YetAnotherConfigLib.Builder? {
                return builder.title(Text.translatable("comet.config.title"))
                    .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("comet.config.group.modules.title"))
                        .option(Option.createBuilder<Boolean>()
                            .name(Text.translatable("comet.config.option.tooltipModule"))
                            .binding(
                                defaults.tooltipModule,
                                { config.tooltipModule },
                                { value: Boolean ->
                                    config.tooltipModule = value
                                }
                            )
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                        ).build()
                    ).category(ConfigCategory.createBuilder()
                        .name(Text.translatable("comet.config.group.tweaks.title"))
                        .option(Option.createBuilder<Boolean>()
                            .name(Text.translatable("comet.config.option.serverNameLengthTweak"))
                            .binding(
                                defaults.serverNameLengthTweak,
                                { config.serverNameLengthTweak },
                                { value: Boolean ->
                                    config.serverNameLengthTweak = value
                                }
                            )
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                        ).build()
                    ).save {
                        INSTANCE.save()
                    }
            }).generateScreen(parent)
        }
    }

    @ConfigEntry
    var tooltipModule: Boolean = true

    @ConfigEntry
    var serverNameLengthTweak: Boolean = true

}
