package com.banglaclient.module.impl.chat;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.StringSetting;

import java.util.Locale;

/**
 * Module 222: Chat Filter
 * Filters unwanted spam or blocked phrases from appearing in client chat.
 */
public final class ChatFilter extends AbstractModule {

    private final StringSetting blockedWordsCsv = registerSetting(new StringSetting(
            "blocked_words_csv", "Blocked Words (CSV)", "Comma-separated words or phrases to hide from chat", "free coins, discord.gg/spam", 256
    ));
    private final BooleanSetting collapseDuplicateSpam = registerSetting(new BooleanSetting(
            "collapse_duplicates", "Block Duplicate Spam", "Suppress consecutive identical chat messages within 5 seconds", true
    ));

    private String lastMessage = "";
    private long lastMessageTimestampMillis = 0L;

    public ChatFilter() {
        super(
                "chat_filter",
                "Chat Filter",
                "Filters unwanted messages and repetitive chat spam.",
                ModuleCategory.CHAT,
                false
        );
    }

    public boolean shouldHideMessage(String rawText) {
        if (!isEnabled() || rawText == null || rawText.isEmpty()) {
            return false;
        }
        String lower = rawText.toLowerCase(Locale.ROOT);
        String[] rules = blockedWordsCsv.getValue().split(",");
        for (String r : rules) {
            String token = r.trim().toLowerCase(Locale.ROOT);
            if (!token.isEmpty() && lower.contains(token)) {
                return true;
            }
        }

        if (collapseDuplicateSpam.isEnabled()) {
            long now = System.currentTimeMillis();
            if (lower.equals(lastMessage) && (now - lastMessageTimestampMillis) < 5000L) {
                return true;
            }
            lastMessage = lower;
            lastMessageTimestampMillis = now;
        }
        return false;
    }
}
