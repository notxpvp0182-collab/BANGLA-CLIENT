package com.banglaclient.module.impl.advanced;

import com.banglaclient.core.ClientCore;
import com.banglaclient.core.HardwareTelemetry;
import com.banglaclient.event.ClientTickEvent;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 285: Thermal Mode
 * Reduces sustained CPU/GPU thermal load when CPU utilization stays above a configured threshold.
 */
public final class ThermalMode extends AbstractModule {

    private final IntegerSetting cpuLoadThreshold = registerSetting(new IntegerSetting(
            "thermal_cpu_threshold", "Sustained CPU Trigger", "Reduce frame rate ceiling if CPU load stays above this %", 88, 65, 98, 2, "%"
    ));
    private final IntegerSetting cooldownFpsCap = registerSetting(new IntegerSetting(
            "thermal_fps_cap", "Thermal FPS Ceiling", "Capped frame rate during thermal relief", 90, 45, 144, 5, " FPS"
    ));

    public ThermalMode() {
        super(
                "thermal_mode",
                "Thermal Mode",
                "Reduces workload when sustained high CPU/GPU load is detected.",
                ModuleCategory.ADVANCED_PERFORMANCE,
                false
        );
    }

    @Override
    public void onEnable() {
        ClientCore.getInstance().getEventBus().subscribe(this, ClientTickEvent.class, this::onTick);
    }

    private void onTick(ClientTickEvent event) {
        if (event.getPhase() != ClientTickEvent.Phase.END || mc.options == null) {
            return;
        }
        HardwareTelemetry hw = HardwareTelemetry.getInstance();
        hw.pollMetrics();
        if (hw.getSystemCpuLoadPercent() >= cpuLoadThreshold.getValue()) {
            if (mc.options.getMaxFps().getValue() > cooldownFpsCap.getValue()) {
                mc.options.getMaxFps().setValue(cooldownFpsCap.getValue());
            }
        }
    }
}
