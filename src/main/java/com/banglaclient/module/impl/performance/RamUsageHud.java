package com.banglaclient.module.impl.performance;

import com.banglaclient.core.HardwareTelemetry;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ModeSetting;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 056: RAM Usage HUD
 * Shows current JVM/client heap memory usage in MB and/or percentage.
 */
public final class RamUsageHud extends HudModule {

    private final ModeSetting displayFormat = registerSetting(new ModeSetting(
            "ram_display_format", "Format", "Memory display format", "Percentage & MB", "PercentageOnly", "MB Only", "Percentage & MB"
    ));

    public RamUsageHud() {
        super(
                "ram_usage_hud",
                "RAM Usage HUD",
                "Shows current JVM/client memory usage.",
                ModuleCategory.PERFORMANCE_FPS,
                false,
                14.0f,
                234.0f,
                114.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        HardwareTelemetry hw = HardwareTelemetry.getInstance();
        hw.pollMetrics();
        String value = switch (displayFormat.getValue()) {
            case "PercentageOnly" -> hw.getMemoryUsagePercent() + "%";
            case "MB Only" -> hw.getUsedMemoryMb() + "/" + hw.getMaxMemoryMb() + " MB";
            default -> hw.getMemoryUsagePercent() + "% (" + hw.getUsedMemoryMb() + " MB)";
        };
        drawSimpleLabelValue(context, "RAM", value);
    }
}
