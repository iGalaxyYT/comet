package dev.igalaxy.comet.mixin;

import dev.igalaxy.comet.Comet;
import net.minecraft.SharedConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SharedConstants.class)
public class SharedConstantsMixin {
	@Inject(method = "isValidChar", at = @At("RETURN"), cancellable = true)
	private static void comet$isValidChar(char chr, CallbackInfoReturnable<Boolean> cir) {
		if (Comet.INSTANCE.getCONFIG().getServerNameLengthEnabled()) {
			cir.setReturnValue(chr >= ' ' && chr != 127);
		}
	}
}
