package com.banglaclient.module.impl.camera;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 160: No View Bobbing
 * Disables walking camera world bobbing while optionally preserving subtle hand bobbing so
 * crosshairs stay dead-center during PvP and bridging.
 */
public final class NoViewBobbing extends AbstractModule {

    private final BooleanSetting keepHandBobbing = registerSetting(new BooleanSetting(
            "keep_hand_bobbing", "Keep Held-Item Sway", "Keep first-person hand sway while keeping the world crosshair stationary", true
    ));

    public NoViewBobbing() {
        super(
                "no_view_bobbing",
                "No View Bobbing",
                "Disables walking camera world bobbing for steady aim.",
                ModuleCategory.CAMERA_VIEWMODEL,
                false
        );
    }

    public boolean shouldCancelWorldBobbing() {
        return isEnabled();
    }

    public boolean isKeepHandBobbing() {
        return isEnabled() && keepHandBobbing.isEnabled();
    }
}
