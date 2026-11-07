package com.banglaclient.module.impl.visual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ColorSetting;
import com.banglaclient.setting.FloatSetting;
import com.banglaclient.theme.ColorData;

/**
 * Module 176: Color Controls
 * Provides global visual tint, temperature, and contrast controls.
 */
public final class ColorControls extends AbstractModule {

    private final ColorSetting worldTint = registerSetting(new ColorSetting(
            "world_tint_color", "World Tint", "Subtle color grading tint", ColorData.WHITE, false
    ));
    private final FloatSetting tintStrength = registerSetting(new FloatSetting(
            "world_tint_strength", "Tint Strength", "Blend factor for custom color grading", 0.0f, 0.0f, 0.5f, 0.02f, ""
    ));

    public ColorControls() {
        super(
                "color_controls",
                "Color Controls",
                "Provides visual color grading and temperature controls.",
                ModuleCategory.VISUAL_GRAPHICS,
                false
        );
    }

    public ColorData getWorldTint() { return worldTint.getValue(); }
    public float getTintStrength() { return isEnabled() ? tintStrength.getValue() : 0.0f; }
}
