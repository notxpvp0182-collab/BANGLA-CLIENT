package com.banglaclient.module.impl.camera;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 162: Damage Tilt Control
 * Controls directional damage camera tilt intensity and syncs with Minecraft's Damage Tilt accessibility option.
 */
public final class DamageTiltControl extends AbstractModule {

    private final FloatSetting tiltStrength = registerSetting(new FloatSetting(
            "damage_tilt_strength", "Tilt Strength", "Directional camera tilt multiplier on hit", 0.25f, 0.0f, 1.0f, 0.05f, "x"
    ));
    private final BooleanSetting directionalYawHint = registerSetting(new BooleanSetting(
            "directional_yaw_hint", "Keep Directional Cue", "Preserve subtle directional tilt while preventing disorienting roll", true
    ));

    public DamageTiltControl() {
        super(
                "damage_tilt_control",
                "Damage Tilt Control",
                "Controls damage camera tilt angle and intensity.",
                ModuleCategory.CAMERA_VIEWMODEL,
                false
        );
        tiltStrength.onChange((oldV, newV) -> applyToGameOptions(newV));
    }

    @Override
    public void onEnable() {
        applyToGameOptions(tiltStrength.getValue());
    }

    private void applyToGameOptions(float val) {
        if (mc.options != null) {
            mc.options.getDamageTiltStrength().setValue((double) val);
        }
    }

    public float getTiltStrength() {
        return isEnabled() ? tiltStrength.getValue() : 1.0f;
    }

    public boolean isDirectionalYawHint() {
        return isEnabled() && directionalYawHint.isEnabled();
    }
}
