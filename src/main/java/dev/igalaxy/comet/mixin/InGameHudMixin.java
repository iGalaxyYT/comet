package dev.igalaxy.comet.mixin;

import dev.igalaxy.comet.Comet;
import dev.igalaxy.comet.modules.hud.CometHudModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicInteger;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	@Inject(at = @At("TAIL"), method = "render")
	private void comet$render(GuiGraphics graphics, float tickDelta, CallbackInfo ci) {
		MinecraftClient client = MinecraftClient.getInstance();

		if (!client.options.debugEnabled && Comet.INSTANCE.getCONFIG().getHudEnabled()) {
			int textPosX = Comet.INSTANCE.getCONFIG().getHudOffsetX();
			int textPosY = Comet.INSTANCE.getCONFIG().getHudOffsetY();

			double guiScale = client.getWindow().getScaleFactor();
			if (guiScale > 0) {
				textPosX /= guiScale;
				textPosY /= guiScale;
			}

			int textColor = 0xFFFFFF;

			int finalTextPosX = textPosX;
			int finalTextPosY = textPosY;
			AtomicInteger lineNum = new AtomicInteger();
			Comet.INSTANCE.getCONFIG().getHudLines().forEach(line -> {
				Text text = line.getText();
				if (text != null) {
					int num = lineNum.getAndIncrement();
					this.renderText(graphics, client.textRenderer, text, finalTextPosX, finalTextPosY + ((client.textRenderer.fontHeight + 2) * num), textColor, 1.0f, true);
				}
			});
		}
	}

	private void renderText(GuiGraphics context, TextRenderer textRenderer, Text text, int x, int y, int color, float scale, boolean shadowed) {
		MatrixStack matrixStack = context.getMatrices();

		matrixStack.push();
		matrixStack.translate(x, y, 0);
		matrixStack.scale(scale, scale, scale);
		matrixStack.translate(-x, -y, 0);

		context.drawText(textRenderer, text, x, y, color, Comet.INSTANCE.getCONFIG().getHudShadow());
		matrixStack.pop();
	}
}
