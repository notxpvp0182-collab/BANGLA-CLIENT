package com.banglaclient.module.impl.worldvisual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ModeSetting;

/**
 * Module 186: Weather Control
 * Controls client-side rain/snow rendering (Server Default, Always Clear, Rain, or Snow).
 */
public final class WeatherControl extends AbstractModule {

    private final ModeSetting weatherMode = registerSetting(new ModeSetting(
            "weather_mode", "Weather State", "Client-side weather rendering override",
            "Always Clear", "Server Default", "Always Clear", "Light Rain", "Snowfall"
    ));

    public WeatherControl() {
        super(
                "weather_control",
                "Weather Control",
                "Controls client-side rain and snow rendering.",
                ModuleCategory.WORLD_VISUAL,
                false
        );
    }

    public boolean shouldCancelWeatherRender() {
        return isEnabled() && weatherMode.isMode("Always Clear");
    }

    public String getWeatherMode() {
        return weatherMode.getValue();
    }
}
