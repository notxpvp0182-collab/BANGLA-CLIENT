package com.banglaclient.module.impl.visual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 172: Sharpening Filter
 * Applies configurable image sharpening / mipmap LOD bias for crisper distant block textures.
 */
public final class SharpeningFilter extends AbstractModule {

    private final FloatSetting sharpnessStrength = registerSetting(new FloatSetting(
            "sharpness_strength", "Sharpness", "Texture crispness factor", 0.5f, 0.1f, 1.0f, 0.05f, ""
    ));

    public SharpeningFilter() {
        super(
                "sharpening_filter",
                "Sharpening Filter",
                "Applies configurable image sharpening for crisp block edges.",
                ModuleCategory.VISUAL_GRAPHICS,
                false
        );
    }

    public float getSharpnessStrength() {
        return isEnabled() ? sharpnessStrength.getValue() : 0.0f;
    }
}
