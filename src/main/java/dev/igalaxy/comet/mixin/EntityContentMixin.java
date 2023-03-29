package dev.igalaxy.comet.mixin;

import dev.igalaxy.comet.modules.pronouns.PronounsManager;
import dev.igalaxy.comet.modules.pronouns.PronounsMessages;
import kotlin.Unit;
import net.minecraft.entity.EntityType;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.UUID;

@Mixin(HoverEvent.EntityContent.class)
public class EntityContentMixin {
	@Shadow
	@Final
	public EntityType<?> entityType;

	@Shadow @Final public UUID uuid;

	@Shadow @Final @Mutable
	@Nullable
	public Text name;

	@Shadow private @Nullable List<Text> tooltip;

	@Inject(method = "asTooltip", at = @At("HEAD"))
	private void modifyTooltip(CallbackInfoReturnable<List<Text>> cir) {
		if (!entityType.equals(EntityType.PLAYER))
			return;

		var pronounsManager = PronounsManager.INSTANCE;
		if (pronounsManager.isPronounsCached(uuid)) {
			var pronouns = pronounsManager.getPronouns(uuid);

			name = PronounsMessages.INSTANCE.getTextWithColoredPronouns(name, pronouns);
		} else if (pronounsManager.isCurrentlyFetching(uuid)) {
			pronounsManager.cachePronouns(uuid, (pronouns) -> {
				tooltip = null;
				return Unit.INSTANCE;
			});
		}
	}
}
