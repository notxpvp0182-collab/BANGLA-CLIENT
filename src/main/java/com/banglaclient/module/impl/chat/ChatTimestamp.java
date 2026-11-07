package com.banglaclient.module.impl.chat;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.ModeSetting;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Module 226: Chat Timestamp
 * Prepends formatted local timestamps (e.g., [14:32:08]) to incoming chat messages.
 */
public final class ChatTimestamp extends AbstractModule {

    private final ModeSetting timePattern = registerSetting(new ModeSetting(
            "chat_ts_pattern", "Timestamp Format", "Time format prefix for chat messages",
            "HH:mm:ss", "HH:mm", "HH:mm:ss", "hh:mm a"
    ));
    private final BooleanSetting crimsonBrackets = registerSetting(new BooleanSetting(
            "crimson_brackets", "Red Accent Prefix", "Style timestamp brackets in subtle gray/red", true
    ));

    private static final DateTimeFormatter FMT_HM = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter FMT_HMS = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter FMT_12H = DateTimeFormatter.ofPattern("hh:mm a");

    public ChatTimestamp() {
        super(
                "chat_timestamp",
                "Chat Timestamp",
                "Adds formatted timestamps to incoming chat messages.",
                ModuleCategory.CHAT,
                false
        );
    }

    public String buildPrefix() {
        DateTimeFormatter fmt = switch (timePattern.getValue()) {
            case "HH:mm" -> FMT_HM;
            case "hh:mm a" -> FMT_12H;
            default -> FMT_HMS;
        };
        String timeStr = LocalTime.now().format(fmt);
        return crimsonBrackets.isEnabled() ? ("§8[§c" + timeStr + "§8] §r") : ("§7[" + timeStr + "] §r");
    }
}
