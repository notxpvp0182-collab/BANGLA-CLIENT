package com.banglaclient.module.impl.camera;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 157: Speed FOV Control
 * Controls speed-attribute and Speed II / Slowness potion FOV warping so high-speed PvP remains aim-consistent.
 */
public final class SpeedFovControl extends AbstractModule {

    private final FloatSetting speedEffectScale = registerSetting(new FloatSetting(
            "speed_effect_scale", "Speed FOV Multiplier", "Scale of Speed/Slowness attribute FOV change", 0.35f, 0.0f, 1.5f, 0.05f, "x"
    ));
    private final FloatSetting maxFovClamp = registerSetting(new FloatSetting(
            "max_fov_clamp", "Max FOV Multiplier", "Upper cap on dynamic speed FOV expansion", 1.15f, 1.0f, 1.5f, 0.05f, "x"
    ));

    public SpeedFovControl() {
        super(
                "speed_fov_control",
                "Speed FOV Control",
                "Controls speed-based and Speed II FOV warping.",
                ModuleCategory.CAMERA_VIEWMODEL,
                false
        );
    }

    public float applySpeedFovClamp(float vanillaMultiplier) {
        if (!isEnabled()) {
            return vanillaMultiplier;
        }
        float delta = (vanillaMultiplier - 1.0f) * speedEffectScale.getValue();
        return Math.min(maxFovClamp.getValue(), 1.0f + delta);
    }
}
