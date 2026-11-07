package com.banglaclient.module.impl.visual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 171: HDR Toggle
 * Provides high-dynamic-range contrast and highlight roll-off control where supported.
 */
public final class HdrToggle extends AbstractModule {

    private final FloatSetting dynamicContrast = registerSetting(new FloatSetting(
            "hdr_contrast", "Dynamic Contrast", "Highlight and shadow dynamic range boost", 1.15f, 1.0f, 1.5f, 0.05f, "x"
    ));

    public HdrToggle() {
        super(
                "hdr_toggle",
                "HDR Toggle",
                "Provides high-dynamic-range contrast control.",
                ModuleCategory.VISUAL_GRAPHICS,
                false
        );
    }

    public float getDynamicContrast() {
        return isEnabled() ? dynamicContrast.getValue() : 1.0f;
    }
}
