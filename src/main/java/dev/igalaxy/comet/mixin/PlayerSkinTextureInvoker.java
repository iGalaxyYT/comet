package dev.igalaxy.comet.mixin;

import com.mojang.blaze3d.texture.NativeImage;
import net.minecraft.client.texture.PlayerSkinTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerSkinTexture.class)
public interface PlayerSkinTextureInvoker {
	@Invoker("stripAlpha")
	static void invokeStripAlpha(NativeImage image, int x1, int y1, int x2, int y2) {
		throw new AssertionError();
	}

	@Invoker("stripColor")
	static void invokeStripColor(NativeImage image, int x1, int y1, int x2, int y2) {
		throw new AssertionError();
	}
}
