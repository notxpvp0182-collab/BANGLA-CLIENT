package com.banglaclient.module.impl.chat;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 231: Chat Background Opacity
 * Controls chat background transparency or enables completely transparent "Clear Chat" background.
 */
public final class ChatBackgroundOpacity extends AbstractModule {

    private final BooleanSetting completelyTransparent = registerSetting(new BooleanSetting(
            "completely_transparent", "Transparent Background", "Remove the dark rectangle behind chat messages completely", false
    ));
    private final FloatSetting opacity = registerSetting(new FloatSetting(
            "chat_bg_opacity", "Background Opacity", "Opacity of the chat box backdrop", 0.45f, 0.0f, 1.0f, 0.05f, ""
    ));

    public ChatBackgroundOpacity() {
        super(
                "chat_background_opacity",
                "Chat Background Opacity",
                "Controls chat background transparency.",
                ModuleCategory.CHAT,
                false
        );
        opacity.visibleWhen(() -> !completelyTransparent.isEnabled());
        completelyTransparent.onChange((o, n) -> syncOpacity());
        opacity.onChange((o, n) -> syncOpacity());
    }

    @Override
    public void onEnable() {
        syncOpacity();
    }

    private void syncOpacity() {
        if (mc.options == null) return;
        double val = completelyTransparent.isEnabled() ? 0.0 : opacity.getValue();
        mc.options.getChatOpacity().setValue(val);
    }

    public boolean isCompletelyTransparent() {
        return isEnabled() && completelyTransparent.isEnabled();
    }
}
