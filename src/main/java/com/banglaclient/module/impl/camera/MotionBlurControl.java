package com.banglaclient.module.impl.camera;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;
import com.banglaclient.setting.ModeSetting;

/**
 * Module 163: Motion Blur Control
 * Controls frame-blend motion blur strength where the rendering pipeline supports it.
 */
public final class MotionBlurControl extends AbstractModule {

    private final FloatSetting blurAmount = registerSetting(new FloatSetting(
            "motion_blur_amount", "Blur Strength", "Strength of camera rotation motion blur", 0.35f, 0.05f, 0.90f, 0.05f, ""
    ));
    private final ModeSetting blurQuality = registerSetting(new ModeSetting(
            "motion_blur_quality", "Sample Quality", "Shader sample quality mode", "Balanced", "Fast", "Balanced", "High"
    ));

    public MotionBlurControl() {
        super(
                "motion_blur_control",
                "Motion Blur Control",
                "Controls motion blur intensity when rotating the camera.",
                ModuleCategory.CAMERA_VIEWMODEL,
                false
        );
    }

    public float getBlurAmount() {
        return isEnabled() ? blurAmount.getValue() : 0.0f;
    }

    public String getBlurQuality() {
        return blurQuality.getValue();
    }
}
