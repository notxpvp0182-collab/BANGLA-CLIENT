package com.banglaclient.module.impl.visual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.ColorSetting;
import com.banglaclient.setting.FloatSetting;
import com.banglaclient.theme.ColorData;

/**
 * Module 178: Custom Fog
 * Customizes horizon fog start/end distance multiplier and custom fog color.
 */
public final class CustomFog extends AbstractModule {

    private final FloatSetting fogStartMultiplier = registerSetting(new FloatSetting(
            "fog_start_mult", "Fog Start Distance", "Multiplier for where horizon fog begins", 1.2f, 0.2f, 3.0f, 0.1f, "x"
    ));
    private final BooleanSetting overrideFogColor = registerSetting(new BooleanSetting(
            "override_fog_color", "Custom Fog Color", "Apply a custom RGB color to horizon fog", false
    ));
    private final ColorSetting fogColor = registerSetting(new ColorSetting(
            "custom_fog_color", "Fog Color", "Color of the custom horizon fog", new ColorData(20, 20, 28, 255), false
    ));

    public CustomFog() {
        super(
                "custom_fog",
                "Custom Fog",
                "Customizes horizon fog appearance, distance, and color.",
                ModuleCategory.VISUAL_GRAPHICS,
                false
        );
        fogColor.visibleWhen(overrideFogColor::isEnabled);
    }

    public float getFogStartMultiplier() { return fogStartMultiplier.getValue(); }
    public boolean isOverrideFogColor() { return isEnabled() && overrideFogColor.isEnabled(); }
    public ColorData getFogColor() { return fogColor.getValue(); }
}
