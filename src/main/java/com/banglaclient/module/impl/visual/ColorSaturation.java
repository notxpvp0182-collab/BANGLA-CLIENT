package com.banglaclient.module.impl.visual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 175: Color Saturation
 * Adjusts visual vibrancy and color saturation of world rendering.
 */
public final class ColorSaturation extends AbstractModule {

    private final FloatSetting saturation = registerSetting(new FloatSetting(
            "color_saturation_val", "Saturation", "Color vibrancy multiplier (1.0 = Vanilla)", 1.25f, 0.0f, 2.0f, 0.05f, "x"
    ));

    public ColorSaturation() {
        super(
                "color_saturation",
                "Color Saturation",
                "Adjusts world color vibrancy and saturation.",
                ModuleCategory.VISUAL_GRAPHICS,
                false
        );
    }

    public float getSaturation() {
        return isEnabled() ? saturation.getValue() : 1.0f;
    }
}
