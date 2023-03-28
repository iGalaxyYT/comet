package dev.igalaxy.comet.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import dev.igalaxy.comet.Comet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
	@Inject(method = "init", at = @At("TAIL"))
	public void exampleMod$onInit(CallbackInfo ci) {
		Comet.INSTANCE.getLOGGER().info("This line is printed by an example mod mixin!");
	}
}
