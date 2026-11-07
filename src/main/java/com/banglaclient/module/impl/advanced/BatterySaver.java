package com.banglaclient.module.impl.advanced;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.IntegerSetting;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.particle.ParticlesMode;

/**
 * Module 284: Battery Saver
 * Reduces GPU/CPU power draw on laptops and battery-powered devices by capping FPS and disabling heavy background effects.
 */
public final class BatterySaver extends AbstractModule {

    private final IntegerSetting batteryFpsCap = registerSetting(new IntegerSetting(
            "battery_fps_cap", "Battery FPS Cap", "Frame rate cap while Battery Saver is enabled", 60, 30, 120, 10, " FPS"
    ));

    public BatterySaver() {
        super(
                "battery_saver",
                "Battery Saver",
                "Reduces CPU/GPU power draw on laptops and battery devices.",
                ModuleCategory.ADVANCED_PERFORMANCE,
                false
        );
    }

    @Override
    public void onEnable() {
        if (mc.options != null) {
            mc.options.getMaxFps().setValue(batteryFpsCap.getValue());
            mc.options.getCloudRenderMode().setValue(CloudRenderMode.OFF);
            mc.options.getParticles().setValue(ParticlesMode.DECREASED);
            OptimizationLog.recordEntry("Enabled Battery Saver (" + batteryFpsCap.getValue() + " FPS Cap)");
        }
    }
}
