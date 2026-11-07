package com.banglaclient.module.impl.worldvisual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 187: Weather Intensity
 * Controls visual rain/thunder gradient intensity and alpha opacity.
 */
public final class WeatherIntensity extends AbstractModule {

    private final FloatSetting intensityMultiplier = registerSetting(new FloatSetting(
            "weather_intensity_mult", "Rain Opacity", "Visual rain and snow opacity multiplier", 0.35f, 0.0f, 1.0f, 0.05f, "x"
    ));

    public WeatherIntensity() {
        super(
                "weather_intensity",
                "Weather Intensity",
                "Controls visual rain and snow intensity.",
                ModuleCategory.WORLD_VISUAL,
                false
        );
    }

    public float getIntensityMultiplier() {
        return isEnabled() ? intensityMultiplier.getValue() : 1.0f;
    }
}
