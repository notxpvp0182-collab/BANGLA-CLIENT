package com.banglaclient.module.impl.chat;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 230: Chat Width/Height
 * Controls chat box width and focused/unfocused height ratios.
 */
public final class ChatWidthHeight extends AbstractModule {

    private final FloatSetting widthRatio = registerSetting(new FloatSetting(
            "chat_width_ratio", "Chat Width", "Chat width ratio (1.0 = 320px standard)", 1.0f, 0.3f, 1.0f, 0.05f, ""
    ));
    private final FloatSetting focusedHeightRatio = registerSetting(new FloatSetting(
            "chat_focused_height", "Focused Height", "Height ratio when chat screen is open", 1.0f, 0.2f, 1.0f, 0.05f, ""
    ));
    private final FloatSetting unfocusedHeightRatio = registerSetting(new FloatSetting(
            "chat_unfocused_height", "Unfocused Height", "Height ratio during gameplay", 0.5f, 0.1f, 1.0f, 0.05f, ""
    ));

    public ChatWidthHeight() {
        super(
                "chat_width_height",
                "Chat Width/Height",
                "Controls chat box dimensions.",
                ModuleCategory.CHAT,
                false
        );
        widthRatio.onChange((o, n) -> syncOptions());
        focusedHeightRatio.onChange((o, n) -> syncOptions());
        unfocusedHeightRatio.onChange((o, n) -> syncOptions());
    }

    @Override
    public void onEnable() {
        syncOptions();
    }

    private void syncOptions() {
        if (mc.options == null) return;
        mc.options.getChatWidth().setValue((double) widthRatio.getValue());
        mc.options.getChatHeightFocused().setValue((double) focusedHeightRatio.getValue());
        mc.options.getChatHeightUnfocused().setValue((double) unfocusedHeightRatio.getValue());
    }
}
