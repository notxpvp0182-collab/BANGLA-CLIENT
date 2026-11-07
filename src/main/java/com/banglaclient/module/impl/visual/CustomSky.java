package com.banglaclient.module.impl.visual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;
import com.banglaclient.setting.ModeSetting;

/**
 * Module 182: Custom Sky
 * Allows configurable client-side sky time-of-day appearance (Day, Sunset, Midnight, or Custom Time).
 */
public final class CustomSky extends AbstractModule {

    private final ModeSetting skyPreset = registerSetting(new ModeSetting(
            "sky_preset", "Sky Preset", "Visual sky time override",
            "Midnight", "Noon Day", "Crimson Sunset", "Midnight", "Custom Time"
    ));
    private final IntegerSetting customTicks = registerSetting(new IntegerSetting(
            "custom_sky_ticks", "Custom Time Ticks", "Exact client time-of-day ticks (0 - 24000)", 18000, 0, 24000, 500, "t"
    ));
    private final BooleanSetting clearWeatherVisuals = registerSetting(new BooleanSetting(
            "clear_sky_weather", "Force Clear Sky Dome", "Keep sky dome clear even during server rain", true
    ));

    public CustomSky() {
        super(
                "custom_sky",
                "Custom Sky",
                "Allows configurable sky time and appearance.",
                ModuleCategory.VISUAL_GRAPHICS,
                false
        );
        customTicks.visibleWhen(() -> skyPreset.isMode("Custom Time"));
    }

    public long getOverriddenTimeOfDay() {
        return switch (skyPreset.getValue()) {
            case "Noon Day" -> 6000L;
            case "Crimson Sunset" -> 12750L;
            case "Custom Time" -> customTicks.getValue();
            default -> 18000L;
        };
    }

    public boolean isClearWeatherVisuals() {
        return isEnabled() && clearWeatherVisuals.isEnabled();
    }
}
