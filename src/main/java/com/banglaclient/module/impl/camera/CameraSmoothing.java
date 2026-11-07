package com.banglaclient.module.impl.camera;

import com.banglaclient.animation.SmoothFloat;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 152: Camera Smoothing
 * Smooths camera rotation and vertical step-up transitions for cinematic or jitter-free gameplay.
 */
public final class CameraSmoothing extends AbstractModule {

    private final FloatSetting smoothnessSpeed = registerSetting(new FloatSetting(
            "camera_smooth_speed", "Response Speed", "Higher values track mouse faster; lower values add cinematic glide", 14.0f, 2.0f, 30.0f, 0.5f, ""
    ));
    private final BooleanSetting smoothEyeHeight = registerSetting(new BooleanSetting(
            "smooth_eye_height", "Smooth Sneak/Step Height", "Smoothly interpolate vertical camera height when sneaking or stepping up stairs", true
    ));

    private final SmoothFloat smoothEyeY = new SmoothFloat(0.0f, 16.0f);

    public CameraSmoothing() {
        super(
                "camera_smoothing",
                "Camera Smoothing",
                "Smooths camera rotation and vertical step transitions.",
                ModuleCategory.CAMERA_VIEWMODEL,
                false
        );
    }

    public float getSmoothedEyeY(float targetY) {
        if (!isEnabled() || !smoothEyeHeight.isEnabled()) {
            smoothEyeY.setImmediate(targetY);
            return targetY;
        }
        smoothEyeY.setTarget(targetY);
        return smoothEyeY.updateAndGet();
    }

    public float getSmoothnessSpeed() {
        return smoothnessSpeed.getValue();
    }
}
