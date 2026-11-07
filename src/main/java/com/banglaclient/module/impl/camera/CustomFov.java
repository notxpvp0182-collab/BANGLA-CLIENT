package com.banglaclient.module.impl.camera;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 155: Custom FOV
 * Customizes base Field of View with fine floating-point precision and optional static FOV lock.
 */
public final class CustomFov extends AbstractModule {

    private final FloatSetting baseFov = registerSetting(new FloatSetting(
            "base_fov", "Base FOV", "Custom Field of View in degrees", 90.0f, 30.0f, 130.0f, 1.0f, "°"
    ));
    private final BooleanSetting staticFovLock = registerSetting(new BooleanSetting(
            "static_fov_lock", "Static FOV Lock", "Ignore all dynamic movement/potion FOV changes", false
    ));

    public CustomFov() {
        super(
                "custom_fov",
                "Custom FOV",
                "Customizes Field of View with fine degree control.",
                ModuleCategory.CAMERA_VIEWMODEL,
                false
        );
    }

    public float getBaseFov() {
        return baseFov.getValue();
    }

    public boolean isStaticFovLock() {
        return isEnabled() && staticFovLock.isEnabled();
    }
}
