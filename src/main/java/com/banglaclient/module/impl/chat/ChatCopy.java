package com.banglaclient.module.impl.chat;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.util.Formatting;

/**
 * Module 224: Chat Copy
 * Allows copying hovered chat messages to the system clipboard via Ctrl + Left-Click or Middle-Click.
 */
public final class ChatCopy extends AbstractModule {

    private final BooleanSetting stripColorCodes = registerSetting(new BooleanSetting(
            "strip_color_codes", "Strip Formatting Codes", "Remove section-sign color codes before copying to clipboard", true
    ));

    public ChatCopy() {
        super(
                "chat_copy",
                "Chat Copy",
                "Allows easy copying of chat messages to clipboard.",
                ModuleCategory.CHAT,
                true
        );
    }

    public void copyToClipboard(String messageText) {
        if (!isEnabled() || mc.keyboard == null || messageText == null || messageText.isEmpty()) {
            return;
        }
        String out = stripColorCodes.isEnabled() ? Formatting.strip(messageText) : messageText;
        if (out != null) {
            mc.keyboard.setClipboard(out);
        }
    }
}
