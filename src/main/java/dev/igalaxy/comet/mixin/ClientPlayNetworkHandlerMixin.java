package dev.igalaxy.comet.mixin;

import dev.igalaxy.comet.modules.pronouns.PronounsManager;
import dev.igalaxy.comet.util.MultithreadingKt;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
	@Shadow @Final private MinecraftClient client;

	@Unique
	private boolean comet$awaitingForChunkPacket = false;

	@Inject(method = "onGameJoin", at = @At("RETURN"))
	private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
		comet$awaitingForChunkPacket = true;
	}

	@Inject(method = "onPlayerListHeader", at = @At("HEAD"))
	private void onPlayerListHeader(PlayerListHeaderS2CPacket packet, CallbackInfo ci) {
		if (!comet$awaitingForChunkPacket)
			return;

		comet$awaitingForChunkPacket = false;

		MultithreadingKt.scheduleAsync(1000, () -> PronounsManager.INSTANCE.bulkCachePronouns(client.getNetworkHandler().getPlayerUuids()));
	}
}
