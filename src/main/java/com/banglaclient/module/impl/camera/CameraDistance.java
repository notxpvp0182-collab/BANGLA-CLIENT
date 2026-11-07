package com.banglaclient.module.impl.camera;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 153: Camera Distance
 * Customizes third-person (F5) camera distance within fair-play bounds (Vanilla = 4.0 blocks).
 */
public final class CameraDistance extends AbstractModule {

    private final FloatSetting thirdPersonDistance = registerSetting(new FloatSetting(
            "third_person_distance", "F5 Camera Distance", "Third-person camera distance in blocks (Vanilla = 4.0m)", 5.0f, 1.5f, 10.0f, 0.25f, "m"
    ));
    private final BooleanSetting smoothZoomIn = registerSetting(new BooleanSetting(
            "smooth_f5_zoom", "Smooth F5 Transition", "Smoothly interpolate camera distance when entering F5", true
    ));

    public CameraDistance() {
        super(
                "camera_distance",
                "Camera Distance",
                "Customizes third-person F5 camera distance.",
                ModuleCategory.CAMERA_VIEWMODEL,
                false
        );
    }

    public float getTargetDistance() {
        return isEnabled() ? thirdPersonDistance.getValue() : 4.0f;
    }

    public boolean isSmoothZoomIn() {
        return isEnabled() && smoothZoomIn.isEnabled();
    }
}
