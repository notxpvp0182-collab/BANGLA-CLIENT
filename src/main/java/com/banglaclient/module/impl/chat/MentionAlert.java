package com.banglaclient.module.impl.chat;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.ColorSetting;
import com.banglaclient.theme.ColorData;
import net.minecraft.sound.SoundEvents;

import java.util.Locale;

/**
 * Module 228: Mention Alert
 * Alerts you with a sound ping and line highlight when your username is mentioned in chat.
 */
public final class MentionAlert extends AbstractModule {

    private final BooleanSetting playPingSound = registerSetting(new BooleanSetting(
            "play_mention_ping", "Play Alert Sound", "Play a note-block chime when your username is mentioned", true
    ));
    private final ColorSetting mentionTint = registerSetting(new ColorSetting(
            "mention_tint_color", "Mention Line Tint", "Background highlight for messages mentioning you", new ColorData(229, 37, 33, 90), true
    ));

    public MentionAlert() {
        super(
                "mention_alert",
                "Mention Alert",
                "Alerts when your player name is mentioned in chat.",
                ModuleCategory.CHAT,
                true
        );
    }

    public boolean checkAndAlert(String rawMessage) {
        if (!isEnabled() || rawMessage == null || mc.getSession() == null) {
            return false;
        }
        String username = mc.getSession().getUsername();
        if (username == null || username.isEmpty()) {
            return false;
        }
        if (rawMessage.toLowerCase(Locale.ROOT).contains(username.toLowerCase(Locale.ROOT))) {
            if (playPingSound.isEnabled() && mc.player != null) {
                mc.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), 0.7f, 1.4f);
            }
            return true;
        }
        return false;
    }

    public ColorData getMentionTint() {
        return mentionTint.getValue();
    }
}
