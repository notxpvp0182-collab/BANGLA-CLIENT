package com.banglaclient.module.impl.camera;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 158: Item FOV Control
 * Controls held-item (Bow draw / Spyglass) FOV zoom intensity.
 */
public final class ItemFovControl extends AbstractModule {

    private final FloatSetting bowZoomMultiplier = registerSetting(new FloatSetting(
            "bow_zoom_mult", "Bow Draw Zoom Scale", "Multiplier for FOV zoom while pulling a bow (1.0 = Vanilla)", 1.0f, 0.0f, 2.0f, 0.1f, "x"
    ));

    public ItemFovControl() {
        super(
                "item_fov_control",
                "Item FOV Control",
                "Controls held-item and bow-draw FOV zoom intensity.",
                ModuleCategory.CAMERA_VIEWMODEL,
                false
        );
    }

    public float getBowZoomMultiplier() {
        return isEnabled() ? bowZoomMultiplier.getValue() : 1.0f;
    }
}
