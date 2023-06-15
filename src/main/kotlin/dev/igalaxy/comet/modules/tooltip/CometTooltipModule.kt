package dev.igalaxy.comet.modules.tooltip

import dev.igalaxy.comet.Comet
import dev.igalaxy.comet.modules.CometModule
import net.minecraft.client.item.TooltipContext
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.trim.ArmorTrimPermutation
import net.minecraft.text.Text
import net.minecraft.text.component.TranslatableComponent
import net.minecraft.util.Identifier
import org.quiltmc.qkl.library.text.*
import org.quiltmc.qsl.tooltip.api.client.ItemTooltipCallback

object CometTooltipModule : CometModule, ItemTooltipCallback {
    override val enabled: Boolean
        get() = Comet.CONFIG.tooltipEnabled

    private val TOOLTIP_FONT = Identifier("comet", "tooltip")

    private fun overrideTrimTooltip(stack: ItemStack, player: PlayerEntity?, lines: MutableList<Text>) {
        if (player != null) {
            val trimHolder = ArmorTrimPermutation.getPermutationFromStack(player.world.registryManager, stack)
            if (trimHolder.isEmpty) return
            val trim = trimHolder.get()

            val i = lines.indexOfFirst {
                val component = it.asComponent()
                component is TranslatableComponent && component.key == "item.minecraft.smithing_template.upgrade"
            }

            val pattern = trim.pattern.value()
            val material = trim.material.value()
            val patternName = pattern.description.string.substringBefore(' ')
            val materialStyle = material.description.style
            val materialName = material.description.string.substringBefore(' ')

            lines.removeAt(i + 2)
            lines.removeAt(i + 1)
            lines[i] = buildText {
                color(Color.GREY) {
                    literal("Trim: ")
                    styled(materialStyle) {
                        literal("$patternName ($materialName)")
                    }
                }
            }
        }
    }

    private val ENCHANTMENT_GROUPS = mapOf(
        'S' to listOf(
            Enchantments.BANE_OF_ARTHROPODS,
            Enchantments.SMITE,
            Enchantments.SHARPNESS
        ),
        'P' to listOf(
            Enchantments.BLAST_PROTECTION,
            Enchantments.FIRE_PROTECTION,
            Enchantments.PROTECTION,
            Enchantments.PROJECTILE_PROTECTION
        ),
        'T' to listOf(
            Enchantments.CHANNELING,
            Enchantments.RIPTIDE,
            Enchantments.LOYALTY
        ),
        'B' to listOf(
            Enchantments.DEPTH_STRIDER,
            Enchantments.FROST_WALKER
        ),
        'F' to listOf(
            Enchantments.FORTUNE,
            Enchantments.SILK_TOUCH
        ),
        'I' to listOf(
            Enchantments.INFINITY
        ),
        'C' to listOf(
            Enchantments.MULTISHOT,
            Enchantments.PIERCING
        )
    )

    private fun isMutuallyExclusiveEnchantment(enchantment: Enchantment): Boolean {
        return ENCHANTMENT_GROUPS.values.find {
            it.contains(enchantment)
        } != null
    }

    private fun getEnchantmentGroup(enchantment: Enchantment): Char {
        return ENCHANTMENT_GROUPS.entries.find {
            it.value.contains(enchantment)
        }?.key ?: '?'
    }

    private fun formatEnchantment(enchantment: Enchantment, level: Int): Text {
        val color =
            if (isMutuallyExclusiveEnchantment(enchantment))
                Color.AQUA
            else if (enchantment.isCursed)
                Color.RED
            else
                Color.GREEN

        val prefix = if (isMutuallyExclusiveEnchantment(enchantment)) buildText {
            font(Identifier("comet", "sga")) {
                literal("${getEnchantmentGroup(enchantment)}.")
            }
            literal(" ")
        } else Text.empty()

        return buildText {
            color(color) {
                text(prefix)
                text(enchantment.getName(level))
            }
        }
    }

    private fun overrideEnchantmentTooltip(stack: ItemStack, lines: MutableList<Text>) {
        var enchantments = EnchantmentHelper.get(stack).keys.toList()//.sortedBy { it.getName(1).string.substringBeforeLast(" ") }
        enchantments = enchantments.sortedBy { it.isCursed }
        enchantments = enchantments.sortedByDescending { isMutuallyExclusiveEnchantment(it) }
        if (enchantments.isEmpty()) return

        val enchantmentLevels = EnchantmentHelper.get(stack)

        fun isEnchantmentText(line: Text): Boolean {
            return enchantments.find { enchantment ->
                line.asComponent() is TranslatableComponent &&
                    enchantment.translationKey == (line.asComponent() as TranslatableComponent).key
            } != null
        }
        val firstEnchantment = lines.indexOfFirst { isEnchantmentText(it) }
        val lastEnchantment = lines.indexOfLast { isEnchantmentText(it) }

        for (i in firstEnchantment..lastEnchantment) {
            val enchantment = enchantments[i - firstEnchantment]
            val pipe = if (i == lastEnchantment) "\\" else "|"
            val color = if (enchantment.isCursed) Color.RED else Color.GREY
            lines[i] = buildText {
                color(Color.GREY) {
                    font(TOOLTIP_FONT) {
                        literal(pipe)
                    }
                    literal(" ")
                    color(color) {
                        text(formatEnchantment(enchantment, enchantmentLevels[enchantment]!!))
                    }
                }
            }
        }
        lines.add(firstEnchantment, buildText {
            color(Color.GREY) {
                literal("Enchantments:")
            }
        })
    }

    private fun overrideAttributes(lines: MutableList<Text>) {
        val startIndex = lines.indexOfFirst {
            val text = it.asComponent()
            text is TranslatableComponent && text.key.startsWith("item.modifiers")
        }
        if (startIndex == -1) return
        lines.add(startIndex, buildText {
            color(Color.GREY) {
                literal("Attributes:")
            }
        })
        lines.forEachIndexed { i, line ->
            val text = line.asComponent()
            if (text is TranslatableComponent) {
                if (text.key.startsWith("item.modifiers")) {
                    val slot = line.string.substringAfter(' ').substringAfter(' ')
                    lines[i] = buildText {
                        color(Color.GREY) {
                            font(TOOLTIP_FONT) {
                                literal("-...")
                            }
                            literal(" ")
                            literal(slot)
                        }
                    }
                }
                if (text.key.startsWith("attribute")) {
                    val nextText = if (lines.size >= i + 2) lines[i + 1].asComponent() else null
                    val outerPipe = if (nextText is TranslatableComponent && (nextText.key.startsWith("item.modifiers") || nextText.key.startsWith("attribute"))) "|" else "\\"
                    val innerPipe = if (nextText is TranslatableComponent && nextText.key.startsWith("attribute")) "|" else "\\"
                    lines[i] = buildText {
                        color(Color.GREY) {
                            font(TOOLTIP_FONT) { literal(outerPipe) }
                            literal("  ")
                            font(TOOLTIP_FONT) { literal(innerPipe) }
                            literal(" ")
                            styled(lines[i].style) {
                                text(lines[i])
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onTooltipRequest(
        stack: ItemStack?,
        player: PlayerEntity?,
        context: TooltipContext?,
        lines: MutableList<Text>?
    ) {
        if (!enabled) return
        if (stack == null) return
        if (lines == null) return

        while (lines.contains(Text.empty())) {
            lines.remove(Text.empty())
        }

        overrideTrimTooltip(stack, player, lines)
        overrideEnchantmentTooltip(stack, lines)
        overrideAttributes(lines)
    }

}
