package com.banglaclient.module.impl.performance;

import com.banglaclient.core.HardwareTelemetry;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ModeSetting;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 055: CPU Usage HUD
 * Shows real-time CPU usage queried safely through platform JMX OperatingSystemMXBean.
 */
public final class CpuUsageHud extends HudModule {

    private final ModeSetting metricSource = registerSetting(new ModeSetting(
            "cpu_metric_source", "CPU Source", "Show System-wide CPU load or JVM Process load", "System", "System", "JVM Process"
    ));

    public CpuUsageHud() {
        super(
                "cpu_usage_hud",
                "CPU Usage HUD",
                "Shows CPU usage through safe platform APIs.",
                ModuleCategory.PERFORMANCE_FPS,
                false,
                14.0f,
                210.0f,
                82.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        HardwareTelemetry hw = HardwareTelemetry.getInstance();
        hw.pollMetrics();
        double val = metricSource.isMode("JVM Process") ? hw.getProcessCpuLoadPercent() : hw.getSystemCpuLoadPercent();
        drawSimpleLabelValue(context, "CPU", String.format("%.0f%%", val));
    }
}
