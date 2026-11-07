package com.banglaclient.module.impl.chat;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 229: Chat Scale
 * Controls chat text scale multiplier and syncs with Minecraft's chat scale option.
 */
public final class ChatScale extends AbstractModule {

    private final FloatSetting scale = registerSetting(new FloatSetting(
            "chat_text_scale", "Chat Text Scale", "Scale multiplier for chat messages", 1.0f, 0.4f, 1.25f, 0.05f, "x"
    ));

    public ChatScale() {
        super(
                "chat_scale",
                "Chat Scale",
                "Controls chat text scale.",
                ModuleCategory.CHAT,
                false
        );
        scale.onChange((oldV, newV) -> applyScale(newV));
    }

    @Override
    public void onEnable() {
        applyScale(scale.getValue());
    }

    private void applyScale(float val) {
        if (mc.options != null) {
            mc.options.getChatScale().setValue((double) Math.min(1.0f, val));
        }
    }

    public float getScale() {
        return isEnabled() ? scale.getValue() : 1.0f;
    }
}
