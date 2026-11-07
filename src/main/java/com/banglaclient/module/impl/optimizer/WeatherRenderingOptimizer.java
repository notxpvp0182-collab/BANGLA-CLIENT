package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 021: Weather Rendering Optimizer
 * Reduces rain and snow column rendering radius and splash particle workload during storms.
 */
public final class WeatherRenderingOptimizer extends AbstractModule {

    private final IntegerSetting weatherRadius = registerSetting(new IntegerSetting(
            "weather_radius", "Precipitation Radius", "Column radius around player to render rain/snow", 4, 1, 10, 1, "m"
    ));
    private final BooleanSetting reduceRainSplashes = registerSetting(new BooleanSetting(
            "reduce_rain_splashes", "Reduce Ground Splashes", "Throttle ground rain splash particles by 70%", true
    ));

    public WeatherRenderingOptimizer() {
        super(
                "weather_rendering_optimizer",
                "Weather Rendering Optimizer",
                "Reduces rain and snow column radius and splash workload.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public int getWeatherRadius() {
        return isEnabled() ? weatherRadius.getValue() : 10;
    }

    public boolean isReduceRainSplashes() {
        return isEnabled() && reduceRainSplashes.isEnabled();
    }
}
