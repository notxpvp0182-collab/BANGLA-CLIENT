package com.banglaclient.module.impl.quality;

import com.banglaclient.core.ClientCore;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 297: GUI Animation Control
 * Enables/disables GUI animations and configures global animation interpolation speed.
 */
public final class GuiAnimationControl extends AbstractModule {

    private final FloatSetting speedMultiplier = registerSetting(new FloatSetting(
            "gui_anim_speed_mult", "Animation Speed", "Speed multiplier for UI transitions and scrollbars", 1.0f, 0.25f, 3.0f, 0.05f, "x"
    ));

    public GuiAnimationControl() {
        super(
                "gui_animation_control",
                "GUI Animation Control",
                "Controls GUI fade, scroll, and hover animations.",
                ModuleCategory.CLIENT_QUALITY_FEATURES,
                true
        );
        speedMultiplier.onChange((oldV, newV) -> ClientCore.getInstance().getClientState().setAnimationSpeed(newV));
    }

    @Override
    public void onEnable() {
        ClientCore.getInstance().getClientState().setAnimationsEnabled(true);
        ClientCore.getInstance().getClientState().setAnimationSpeed(speedMultiplier.getValue());
    }

    @Override
    public void onDisable() {
        ClientCore.getInstance().getClientState().setAnimationsEnabled(false);
    }
}
