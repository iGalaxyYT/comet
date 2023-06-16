package dev.igalaxy.comet.config

import dev.igalaxy.comet.modules.discord.CometDiscordModule
import dev.igalaxy.comet.modules.hud.CometHudLine
import dev.isxander.yacl3.api.*
import dev.isxander.yacl3.api.controller.EnumControllerBuilder
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder
import dev.isxander.yacl3.api.controller.StringControllerBuilder
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import dev.isxander.yacl3.config.ConfigEntry
import dev.isxander.yacl3.config.GsonConfigInstance
import dev.isxander.yacl3.gui.controllers.cycling.EnumController
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import org.quiltmc.loader.api.QuiltLoader

class CometConfig {
    companion object {
        val INSTANCE: GsonConfigInstance<CometConfig> = GsonConfigInstance.createBuilder(CometConfig::class.java)
            .setPath(QuiltLoader.getConfigDir().resolve("comet.json"))
            .build()

        fun makeScreen(parent: Screen): Screen {
            return YetAnotherConfigLib.create(INSTANCE, fun(defaults: CometConfig, config: CometConfig, builder: YetAnotherConfigLib.Builder): YetAnotherConfigLib.Builder? {
                return builder.title(Text.translatable("comet.config"))
                    .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("comet.config.modules"))
                        .group(OptionGroup.createBuilder()
                            .name(Text.translatable("comet.config.modules.tooltip"))
                            .description(OptionDescription.of(Text.translatable("comet.config.modules.tooltip.description")))
                            .option(Option.createBuilder<Boolean>()
                                .name(Text.translatable("comet.config.modules.tooltip.enabled"))
                                .binding(
                                    defaults.tooltipEnabled,
                                    { config.tooltipEnabled },
                                    { value: Boolean ->
                                        config.tooltipEnabled = value
                                    }
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build()
                            ).build()
                        )
                        .group(OptionGroup.createBuilder()
                            .name(Text.translatable("comet.config.modules.serverNameLength"))
                            .description(OptionDescription.of(Text.translatable("comet.config.modules.serverNameLength.description")))
                            .option(Option.createBuilder<Boolean>()
                                .name(Text.translatable("comet.config.modules.serverNameLength.enabled"))
                                .binding(
                                    defaults.serverNameLengthEnabled,
                                    { config.serverNameLengthEnabled },
                                    { value: Boolean ->
                                        config.serverNameLengthEnabled = value
                                    }
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build()
                            ).build()
                        )
                        .group(OptionGroup.createBuilder()
                            .name(Text.translatable("comet.config.modules.discord"))
                            .description(OptionDescription.of(Text.translatable("comet.config.modules.discord.description")))
                            .option(Option.createBuilder<Boolean>()
                                .name(Text.translatable("comet.config.modules.discord.enabled"))
                                .binding(
                                    defaults.discordEnabled,
                                    { config.discordEnabled },
                                    { value: Boolean ->
                                        config.discordEnabled = value
                                        CometDiscordModule.checkClient()
                                    }
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build()
                            )
                            .option(Option.createBuilder<String>()
                                .name(Text.translatable("comet.config.modules.discord.client"))
                                .binding(
                                    defaults.discordClient,
                                    { config.discordClient },
                                    { value: String ->
                                        config.discordClient = value
                                    }
                                )
                                .controller(StringControllerBuilder::create)
                                .build()
                            ).build()
                        )
                        .group(OptionGroup.createBuilder()
                            .name(Text.translatable("comet.config.modules.unlockedSkinLoader"))
                            .description(OptionDescription.of(Text.translatable("comet.config.modules.unlockedSkinLoader.description")))
                            .option(Option.createBuilder<Boolean>()
                                .name(Text.translatable("comet.config.modules.unlockedSkinLoader.enabled"))
                                .binding(
                                    defaults.unlockedSkinLoaderEnabled,
                                    { config.unlockedSkinLoaderEnabled },
                                    { value: Boolean ->
                                        config.unlockedSkinLoaderEnabled = value
                                    }
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build()
                            ).build()
                        )
                        .group(OptionGroup.createBuilder()
                            .name(Text.translatable("comet.config.modules.hud"))
                            .description(OptionDescription.of(Text.translatable("comet.config.modules.hud.description")))
                            .option(Option.createBuilder<Boolean>()
                                .name(Text.translatable("comet.config.modules.hud.enabled"))
                                .binding(
                                    defaults.hudEnabled,
                                    { config.hudEnabled },
                                    { value: Boolean ->
                                        config.hudEnabled = value
                                    }
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build()
                            ).option(Option.createBuilder<Int>()
                                .name(Text.translatable("comet.config.modules.hud.offsetX"))
                                .binding(
                                    defaults.hudOffsetX,
                                    { config.hudOffsetX },
                                    { value: Int ->
                                        config.hudOffsetX = value
                                    }
                                )
                                .controller(IntegerFieldControllerBuilder::create)
                                .build()
                            ).option(Option.createBuilder<Int>()
                                .name(Text.translatable("comet.config.modules.hud.offsetY"))
                                .binding(
                                    defaults.hudOffsetY,
                                    { config.hudOffsetY },
                                    { value: Int ->
                                        config.hudOffsetY = value
                                    }
                                )
                                .controller(IntegerFieldControllerBuilder::create)
                                .build()
                            ).option(Option.createBuilder<Boolean>()
                                .name(Text.translatable("comet.config.modules.hud.shadow"))
                                .binding(
                                    defaults.hudShadow,
                                    { config.hudShadow },
                                    { value: Boolean ->
                                        config.hudShadow = value
                                    }
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build()
                            )
                            .build()
                        )
                        .group(ListOption.createBuilder<CometHudLine>()
                            .name(Text.translatable("comet.config.modules.hud.lines"))
                            .description(OptionDescription.of(Text.translatable("comet.config.modules.hud.lines.description")))
                            .binding(
                                defaults.hudLines,
                                { config.hudLines },
                                { value: List<CometHudLine> ->
                                    config.hudLines = value
                                }
                            )
                            .customController {
                                EnumController(it, CometHudLine::class.java)
                            }
                            .initial(CometHudLine.FPS)
                            .build()
                        )
                        .build()
                    ).save {
                        INSTANCE.save()
                    }
            }).generateScreen(parent)
        }
    }

    @ConfigEntry
    var tooltipEnabled: Boolean = false

    @ConfigEntry
    var serverNameLengthEnabled: Boolean = true

    @ConfigEntry
    var discordEnabled: Boolean = false

    @ConfigEntry
    var discordClient: String = "856709531668971551"

    @ConfigEntry
    var unlockedSkinLoaderEnabled: Boolean = true

    @ConfigEntry
    var hudEnabled: Boolean = false

    @ConfigEntry
    var hudOffsetX: Int = 24

    @ConfigEntry
    var hudOffsetY: Int = 24

    @ConfigEntry
    var hudShadow: Boolean = true

    @ConfigEntry
    var hudLines: List<CometHudLine> = listOf()

}
