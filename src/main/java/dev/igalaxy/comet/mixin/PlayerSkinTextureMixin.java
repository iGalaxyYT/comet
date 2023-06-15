package dev.igalaxy.comet.mixin;

import com.mojang.blaze3d.texture.NativeImage;
import net.minecraft.client.texture.PlayerSkinTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static dev.igalaxy.comet.mixin.PlayerSkinTextureInvoker.invokeStripAlpha;
import static dev.igalaxy.comet.mixin.PlayerSkinTextureInvoker.invokeStripColor;

@Mixin(PlayerSkinTexture.class)
public class PlayerSkinTextureMixin {
	@Inject(method = "remapTexture", at = @At("HEAD"), cancellable = true)
	private void comet$remapTexture(NativeImage skinImage, CallbackInfoReturnable<NativeImage> cir) {
		boolean bl = skinImage.getHeight() == 32;
		if (bl) {
			NativeImage nativeImage = new NativeImage(64, 64, true);
			nativeImage.copyFrom(skinImage);
			skinImage.close();
			skinImage = nativeImage;
			nativeImage.fillRect(0, 32, 64, 32, 0);
			nativeImage.copyRectangle(4, 16, 16, 32, 4, 4, true, false);
			nativeImage.copyRectangle(8, 16, 16, 32, 4, 4, true, false);
			nativeImage.copyRectangle(0, 20, 24, 32, 4, 12, true, false);
			nativeImage.copyRectangle(4, 20, 16, 32, 4, 12, true, false);
			nativeImage.copyRectangle(8, 20, 8, 32, 4, 12, true, false);
			nativeImage.copyRectangle(12, 20, 16, 32, 4, 12, true, false);
			nativeImage.copyRectangle(44, 16, -8, 32, 4, 4, true, false);
			nativeImage.copyRectangle(48, 16, -8, 32, 4, 4, true, false);
			nativeImage.copyRectangle(40, 20, 0, 32, 4, 12, true, false);
			nativeImage.copyRectangle(44, 20, -8, 32, 4, 12, true, false);
			nativeImage.copyRectangle(48, 20, -16, 32, 4, 12, true, false);
			nativeImage.copyRectangle(52, 20, -8, 32, 4, 12, true, false);
		}

		invokeStripAlpha(skinImage, 0, 0, 32, 16);
		if (bl) {
			invokeStripColor(skinImage, 32, 0, 64, 32);
		}

		invokeStripAlpha(skinImage, 0, 16, 64, 32);
		invokeStripAlpha(skinImage, 16, 48, 48, 64);
		cir.setReturnValue(skinImage);
		cir.cancel();
	}
}
