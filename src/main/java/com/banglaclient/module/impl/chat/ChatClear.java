package com.banglaclient.module.impl.chat;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ActionSetting;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 225: Chat Clear
 * Clears in-game chat quickly while optionally preserving sent command history.
 */
public final class ChatClear extends AbstractModule {

    private final BooleanSetting preserveSentHistory = registerSetting(new BooleanSetting(
            "preserve_sent_history", "Keep Sent Commands", "Preserve Up-Arrow command history when clearing chat", true
    ));

    public ChatClear() {
        super(
                "chat_clear",
                "Chat Clear",
                "Clears chat messages quickly.",
                ModuleCategory.CHAT,
                false
        );

        registerSetting(new ActionSetting(
                "clear_chat_now",
                "Clear Chat History",
                "Immediately clear visible in-game chat lines",
                "Clear Chat Now",
                this::clearChat
        ));
    }

    public void clearChat() {
        if (mc.inGameHud != null && mc.inGameHud.getChatHud() != null) {
            mc.inGameHud.getChatHud().clear(!preserveSentHistory.isEnabled());
        }
    }
}
