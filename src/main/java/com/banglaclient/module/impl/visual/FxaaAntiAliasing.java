package com.banglaclient.module.impl.visual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 170: FXAA Anti-Aliasing
 * Provides fast approximate anti-aliasing edge smoothing configuration where compatible.
 */
public final class FxaaAntiAliasing extends AbstractModule {

    private final FloatSetting subpixelBlend = registerSetting(new FloatSetting(
            "fxaa_subpixel_blend", "Edge Smoothing", "Sub-pixel edge anti-aliasing blend factor", 0.75f, 0.25f, 1.0f, 0.05f, ""
    ));

    public FxaaAntiAliasing() {
        super(
                "fxaa_anti_aliasing",
                "FXAA Anti-Aliasing",
                "Provides fast edge smoothing anti-aliasing.",
                ModuleCategory.VISUAL_GRAPHICS,
                false
        );
    }

    public float getSubpixelBlend() {
        return isEnabled() ? subpixelBlend.getValue() : 0.0f;
    }
}
