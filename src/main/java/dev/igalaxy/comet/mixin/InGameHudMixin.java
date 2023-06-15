package dev.igalaxy.comet.mixin;

import dev.igalaxy.comet.Comet;
import dev.igalaxy.comet.modules.hud.CometHudModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	@Inject(at = @At("TAIL"), method = "render")
	private void comet$render(GuiGraphics graphics, float tickDelta, CallbackInfo ci) {
		MinecraftClient client = MinecraftClient.getInstance();

		if (!client.options.debugEnabled && Comet.INSTANCE.getCONFIG().getHudEnabled()) {
			String displayText = CometHudModule.INSTANCE.getHudText();
			int textPosX = Comet.INSTANCE.getCONFIG().getHudOffsetX();
			int textPosY = Comet.INSTANCE.getCONFIG().getHudOffsetY();

			double guiScale = client.getWindow().getScaleFactor();
			if (guiScale > 0) {
				textPosX /= guiScale;
				textPosY /= guiScale;
			}

			int maxTextPosX = client.getWindow().getScaledWidth() - client.textRenderer.getWidth(displayText);
			int maxTextPosY = client.getWindow().getScaledHeight() - client.textRenderer.fontHeight;
			textPosX = Math.min(textPosX, maxTextPosX);
			textPosY = Math.min(textPosY, maxTextPosY);

			int textColor = 0xFFFFFF;

			this.renderText(graphics, client.textRenderer, displayText, textPosX, textPosY, textColor, 1.0f, true);
		}
	}

	private void renderText(GuiGraphics context, TextRenderer textRenderer, String text, int x, int y, int color, float scale, boolean shadowed) {
		MatrixStack matrixStack = context.getMatrices();

		matrixStack.push();
		matrixStack.translate(x, y, 0);
		matrixStack.scale(scale, scale, scale);
		matrixStack.translate(-x, -y, 0);

		context.drawText(textRenderer, text, x, y, color, Comet.INSTANCE.getCONFIG().getHudShadow());
		matrixStack.pop();
	}
}
