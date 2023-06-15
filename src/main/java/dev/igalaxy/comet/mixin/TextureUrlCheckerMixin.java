package dev.igalaxy.comet.mixin;

import com.mojang.authlib.yggdrasil.TextureUrlChecker;
import dev.igalaxy.comet.Comet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TextureUrlChecker.class)
public class TextureUrlCheckerMixin {
	@Inject(method = "isAllowedTextureDomain", at = @At("HEAD"), cancellable = true, remap = false)
	private static void comet$isAllowedTextureDomain(String url, CallbackInfoReturnable<Boolean> cir) {
		if (Comet.INSTANCE.getCONFIG().getUnlockedSkinLoaderEnabled()) {
			cir.setReturnValue(true);
			cir.cancel();
		}
	}
}
