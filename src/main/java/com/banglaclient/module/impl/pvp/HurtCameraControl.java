package com.banglaclient.module.impl.pvp;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 116: Hurt Camera Control
 * Controls or reduces the directional screen shake (tilt) when taking damage in combat.
 */
public final class HurtCameraControl extends AbstractModule {

    private final FloatSetting hurtShakeStrength = registerSetting(new FloatSetting(
            "hurt_shake_strength", "Hurt Shake Multiplier", "Multiplier for damage camera tilt (0.0 = No Hurt Shake)", 0.25f, 0.0f, 1.0f, 0.05f, "x"
    ));
    private final BooleanSetting disableFireTickShake = registerSetting(new BooleanSetting(
            "disable_fire_shake", "Ignore Fire/Poison Tilt", "Suppress camera tilt from fire or poison damage ticks", true
    ));

    public HurtCameraControl() {
        super(
                "hurt_camera_control",
                "Hurt Camera Control",
                "Controls hurt camera shake intensity.",
                ModuleCategory.PVP_COMBAT,
                true
        );
    }

    public float getHurtShakeMultiplier() {
        return isEnabled() ? hurtShakeStrength.getValue() : 1.0f;
    }

    public boolean isDisableFireTickShake() {
        return isEnabled() && disableFireTickShake.isEnabled();
    }
}
