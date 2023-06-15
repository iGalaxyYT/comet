package dev.igalaxy.comet.mixin;

import dev.igalaxy.comet.Comet;
import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AddServerScreen.class)
public class AddServerScreenMixin {
	@Shadow
	private TextFieldWidget serverNameField;

	@Inject(method = "init", at = @At("TAIL"))
	private void comet$init(CallbackInfo ci) {
		if (Comet.INSTANCE.getCONFIG().getServerNameLengthEnabled()) {
			this.serverNameField.setMaxLength(Integer.MAX_VALUE);
		}
	}
}
