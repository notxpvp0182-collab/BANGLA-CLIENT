package com.banglaclient.module.impl.camera;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 164: Potion Camera Effect Off
 * Disables disorienting Nausea screen warp and Darkness pulsing camera distortion.
 */
public final class PotionCameraEffectOff extends AbstractModule {

    private final BooleanSetting disableNauseaWarp = registerSetting(new BooleanSetting(
            "disable_nausea_warp", "No Nausea Wobble", "Prevent Nausea portal/potion screen wobble distortion", true
    ));
    private final BooleanSetting reduceDarknessPulse = registerSetting(new BooleanSetting(
            "reduce_darkness_pulse", "Stabilize Darkness Pulse", "Use Minecraft accessibility FOV/distortion scale to prevent motion sickness", true
    ));

    public PotionCameraEffectOff() {
        super(
                "potion_camera_effect_off",
                "Potion Camera Effect Off",
                "Disables disorienting Nausea and potion camera distortion.",
                ModuleCategory.CAMERA_VIEWMODEL,
                true
        );
    }

    @Override
    public void onEnable() {
        if (mc.options != null && disableNauseaWarp.isEnabled()) {
            mc.options.getDistortionEffectScale().setValue(0.0);
        }
    }

    @Override
    public void onDisable() {
        if (mc.options != null) {
            mc.options.getDistortionEffectScale().setValue(1.0);
        }
    }

    public boolean shouldCancelNauseaOverlay() {
        return isEnabled() && disableNauseaWarp.isEnabled();
    }

    public boolean isReduceDarknessPulse() {
        return isEnabled() && reduceDarknessPulse.isEnabled();
    }
}
