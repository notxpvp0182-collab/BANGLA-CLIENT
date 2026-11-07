package com.banglaclient.mixin;

import com.banglaclient.core.ClientCore;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.module.impl.chat.AutoGg;
import com.banglaclient.module.impl.chat.ChatFilter;
import com.banglaclient.module.impl.chat.ChatTimestamp;
import com.banglaclient.module.impl.chat.MentionAlert;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hooks ChatHud#addMessage for:
 * - ChatFilter (Cancelling blocked spam or duplicate messages)
 * - MentionAlert (Playing sound chime when username is mentioned)
 * - AutoGg (Triggering sportsmanship GG on match completion with cooldown)
 * - ChatTimestamp (Prepending formatted timestamp prefix)
 */
@Mixin(ChatHud.class)
public abstract class ChatHudMixin {

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), cancellable = true)
    private void banglaclient$onIncomingMessageHead(Text message, CallbackInfo ci) {
        if (ClientCore.getInstance().getClientState().isVanillaMode() || message == null) {
            return;
        }
        String raw = message.getString();
        ModuleRegistry reg = ModuleRegistry.getInstance();

        var filter = reg.getByClass(ChatFilter.class);
        if (filter.isPresent() && filter.get().shouldHideMessage(raw)) {
            ci.cancel();
            return;
        }

        reg.getByClass(MentionAlert.class).ifPresent(m -> m.checkAndAlert(raw));
        reg.getByClass(AutoGg.class).ifPresent(m -> m.onIncomingSystemChat(raw));
    }

    @ModifyVariable(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), argsOnly = true)
    private Text banglaclient$prependChatTimestamp(Text original) {
        if (ClientCore.getInstance().getClientState().isVanillaMode() || original == null) {
            return original;
        }
        var ts = ModuleRegistry.getInstance().getByClass(ChatTimestamp.class);
        if (ts.isPresent() && ts.get().isEnabled()) {
            return Text.literal(ts.get().buildPrefix()).append(original);
        }
        return original;
    }
}
