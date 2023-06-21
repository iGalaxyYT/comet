package dev.igalaxy.comet.mixin;

import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import dev.igalaxy.comet.modules.emoji.CometEmojiModule;
import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.ArrayList;
import java.util.Collection;

@Mixin(CommandSuggestor.class)
public abstract class CommandSuggestorMixin {

	@Shadow
	public static int getLastPlayerNameStart(String input) {
		return 0;
	}

	@Shadow
	@Final
	private TextFieldWidget textField;

	@Shadow
	public abstract void showSuggestions(boolean narrateFirstSuggestion);

	@SuppressWarnings("InvalidInjectorMethodSignature")
	@ModifyVariable(method = "refresh", at = @At("STORE"), ordinal = 0, name = "collection")
	private Collection<String> comet$modifySuggestionsOutsideOfCommands(Collection<String> vanillaSuggestions) {
		ArrayList<String> newSuggestions = new ArrayList<>(vanillaSuggestions);

		if (CometEmojiModule.INSTANCE.getEnabled()) {
			String currentInput = this.textField.getText();
			int currentCursorPosition = this.textField.getCursor();

			String textBeforeCursor = currentInput.substring(0, currentCursorPosition);
			int startOfCurrentWord = getLastPlayerNameStart(textBeforeCursor);

			String currentWord = textBeforeCursor.substring(startOfCurrentWord);

			Collection<String> additionalSuggestions = CometEmojiModule.INSTANCE.getSuggestions(currentWord);

			newSuggestions.addAll(additionalSuggestions);
		}

		return newSuggestions;
	}

	@Inject(method = "refresh", at = @At("TAIL"))
	private void comet$modifyRefresh(CallbackInfo ci) {
		if (CometEmojiModule.INSTANCE.getEnabled()) {
			String currentInput = this.textField.getText();
			int currentCursorPosition = this.textField.getCursor();

			String textBeforeCursor = currentInput.substring(0, currentCursorPosition);
			int startOfCurrentWord = getLastPlayerNameStart(textBeforeCursor);

			String currentWord = textBeforeCursor.substring(startOfCurrentWord);

			if (currentWord.startsWith(":")) {
				this.showSuggestions(true);
			}

			if (!EmojiParser.parseToUnicode(currentWord).equals(currentWord)) {
				this.textField.setText(EmojiParser.parseToUnicode(this.textField.getText()));
			}
		}
	}

}
