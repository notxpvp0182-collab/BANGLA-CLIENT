package com.banglaclient.module.impl.visual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.ColorSetting;
import com.banglaclient.theme.ColorData;

/**
 * Module 183: Sky Color Wheel
 * Provides an interactive custom RGB/HSB sky and horizon color picker.
 */
public final class SkyColorWheel extends AbstractModule {

    private final ColorSetting zenithColor = registerSetting(new ColorSetting(
            "sky_zenith_color", "Upper Sky Color", "Custom RGB color for the upper sky dome", new ColorData(18, 18, 28, 255), false
    ));
    private final BooleanSetting syncHorizonFog = registerSetting(new BooleanSetting(
            "sync_horizon_fog", "Blend Horizon Color", "Match horizon fog to the custom sky color", true
    ));

    public SkyColorWheel() {
        super(
                "sky_color_wheel",
                "Sky Color Wheel",
                "Provides a custom sky color picker.",
                ModuleCategory.VISUAL_GRAPHICS,
                false
        );
    }

    public ColorData getZenithColor() {
        return zenithColor.getValue();
    }

    public boolean isSyncHorizonFog() {
        return isEnabled() && syncHorizonFog.isEnabled();
    }
}
