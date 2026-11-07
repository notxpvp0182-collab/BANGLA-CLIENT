package com.banglaclient.module.impl.advanced;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 287: Low Power Mode
 * Reduces overall system power consumption by combining conservative view distance, entity shadows off,
 * and a steady frame rate cap.
 */
public final class LowPowerMode extends AbstractModule {

    private final IntegerSetting powerFpsLimit = registerSetting(new IntegerSetting(
            "low_power_fps_limit", "FPS Limit", "Maximum frame rate in Low Power Mode", 60, 30, 90, 5, " FPS"
    ));
    private final BooleanSetting disableEntityShadows = registerSetting(new BooleanSetting(
            "low_power_no_shadows", "Disable Entity Shadows", "Turn off entity ground shadows to save GPU power", true
    ));

    public LowPowerMode() {
        super(
                "low_power_mode",
                "Low Power Mode",
                "Reduces overall CPU and GPU power consumption.",
                ModuleCategory.ADVANCED_PERFORMANCE,
                false
        );
    }

    @Override
    public void onEnable() {
        if (mc.options != null) {
            mc.options.getMaxFps().setValue(powerFpsLimit.getValue());
            if (disableEntityShadows.isEnabled()) {
                mc.options.getEntityShadows().setValue(false);
            }
            OptimizationLog.recordEntry("Enabled Low Power Mode");
        }
    }
}
