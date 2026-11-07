package com.banglaclient.module.impl.visual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 173: Brightness Control
 * Fine-grained gamma/brightness slider from Moody (0%) up to 500%.
 */
public final class BrightnessControl extends AbstractModule {

    private final FloatSetting brightnessPercent = registerSetting(new FloatSetting(
            "brightness_pct", "Brightness Level", "Custom brightness percentage", 150.0f, 0.0f, 500.0f, 10.0f, "%"
    ));

    public BrightnessControl() {
        super(
                "brightness_control",
                "Brightness Control",
                "Controls custom world gamma and brightness percentage.",
                ModuleCategory.VISUAL_GRAPHICS,
                false
        );
    }

    public float getGammaMultiplier() {
        return isEnabled() ? (brightnessPercent.getValue() / 100.0f) : 1.0f;
    }
}
