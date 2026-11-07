package com.banglaclient.module.impl.chat;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.IntegerSetting;
import com.banglaclient.setting.StringSetting;

import java.util.Locale;

/**
 * Module 232: Auto GG
 * Sends a configurable "gg" message after match completion announcements with strict cooldown
 * protection to prevent chat spam.
 */
public final class AutoGg extends AbstractModule {

    private final StringSetting ggMessage = registerSetting(new StringSetting(
            "auto_gg_message", "GG Message", "Sportsmanship message sent at match end", "gg", 32
    ));
    private final IntegerSetting cooldownSeconds = registerSetting(new IntegerSetting(
            "auto_gg_cooldown_sec", "Spam Guard Cooldown", "Minimum seconds between automatic GG messages", 15, 5, 60, 5, "s"
    ));

    private static final String[] MATCH_END_TRIGGERS = new String[]{
            "1st killer -",
            "winner:",
            "winning team -",
            "match summary",
            "victory!"
    };

    private long lastSentMillis = 0L;

    public AutoGg() {
        super(
                "auto_gg",
                "Auto GG",
                "Sends configurable GG messages after match completion with cooldown.",
                ModuleCategory.CHAT,
                false
        );
    }

    public void onIncomingSystemChat(String rawText) {
        if (!isEnabled() || rawText == null || mc.player == null || mc.getNetworkHandler() == null) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now - lastSentMillis < (cooldownSeconds.getValue() * 1000L)) {
            return;
        }
        String lower = rawText.toLowerCase(Locale.ROOT);
        for (String trigger : MATCH_END_TRIGGERS) {
            if (lower.contains(trigger)) {
                lastSentMillis = now;
                mc.getNetworkHandler().sendChatMessage(ggMessage.getValue());
                break;
            }
        }
    }
}
