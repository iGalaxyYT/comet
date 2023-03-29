package dev.igalaxy.comet.mixin;

import dev.igalaxy.comet.modules.pronouns.PronounsManager;
import dev.igalaxy.comet.modules.pronouns.PronounsMessages;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.multiplayer.SocialInteractionsPlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

@Mixin(SocialInteractionsPlayerListEntry.class)
public class SocialInteractionsPlayerListEntryMixin {
	@Shadow
	@Final
	private UUID uuid;

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Ljava/lang/String;FFI)I"))
	private int replaceText(TextRenderer textRenderer, MatrixStack matrices, String text, float x, float y, int color) {
		var pronounsManager = PronounsManager.INSTANCE;
		if (pronounsManager.isPronounsCached(this.uuid)) {
			Text name = PronounsMessages.INSTANCE.getTextWithColoredPronouns(Text.of(text), pronounsManager.getPronouns(this.uuid));
			return textRenderer.draw(matrices, name, x, y, color);
		} else if (!pronounsManager.isCurrentlyFetching(this.uuid)) {
			pronounsManager.cachePronouns(this.uuid);
		}

		return textRenderer.draw(matrices, text, x, y, color);
	}
}
