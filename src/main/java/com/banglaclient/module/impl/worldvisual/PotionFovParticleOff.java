package com.banglaclient.module.impl.worldvisual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 195: Potion FOV Particle Off
 * Controls both potion-induced FOV scaling and camera-blocking beacon/conduit ambient swirls in one module.
 */
public final class PotionFovParticleOff extends AbstractModule {

    private final FloatSetting fovEffectScale = registerSetting(new FloatSetting(
            "potion_fov_effect_scale", "FOV Effect Scale", "Minecraft FOV Effect Scale (0.0 = No Potion FOV Shift)", 0.0f, 0.0f, 1.0f, 0.05f, ""
    ));
    private final BooleanSetting hideBeaconSwirls = registerSetting(new BooleanSetting(
            "hide_beacon_swirls", "Hide Beacon Ambient Swirls", "Suppress translucent beacon status particles around your screen", true
    ));

    public PotionFovParticleOff() {
        super(
                "potion_fov_particle_off",
                "Potion FOV Particle Off",
                "Controls potion-related camera FOV shifts and ambient swirls.",
                ModuleCategory.WORLD_VISUAL,
                false
        );
        fovEffectScale.onChange((oldV, newV) -> applyFovScale(newV));
    }

    @Override
    public void onEnable() {
        applyFovScale(fovEffectScale.getValue());
    }

    private void applyFovScale(float val) {
        if (mc.options != null) {
            mc.options.getFovEffectScale().setValue((double) val);
        }
    }

    public boolean isHideBeaconSwirls() {
        return isEnabled() && hideBeaconSwirls.isEnabled();
    }
}
