package com.banglaclient.module.impl.camera;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 161: No Camera Shake
 * Disables unnecessary explosion, warden sonic-boom, and damage camera shake effects.
 */
public final class NoCameraShake extends AbstractModule {

    private final BooleanSetting suppressExplosionShake = registerSetting(new BooleanSetting(
            "suppress_explosion_shake", "No Explosion Shake", "Prevent screen shake from nearby TNT and End Crystals", true
    ));
    private final BooleanSetting suppressDamageShake = registerSetting(new BooleanSetting(
            "suppress_damage_shake", "No Hurt Shake", "Completely remove directional camera roll on damage", true
    ));

    public NoCameraShake() {
        super(
                "no_camera_shake",
                "No Camera Shake",
                "Disables unnecessary camera shake during explosions and combat.",
                ModuleCategory.CAMERA_VIEWMODEL,
                false
        );
    }

    public boolean shouldCancelShake() {
        return isEnabled() && (suppressExplosionShake.isEnabled() || suppressDamageShake.isEnabled());
    }
}
