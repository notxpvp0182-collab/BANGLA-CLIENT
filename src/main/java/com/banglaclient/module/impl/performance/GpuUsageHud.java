package com.banglaclient.module.impl.performance;

import com.banglaclient.core.HardwareTelemetry;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 054: GPU Usage HUD
 * Shows GPU utilization / frame-budget load and optional OpenGL renderer name.
 */
public final class GpuUsageHud extends HudModule {

    private final BooleanSetting showGpuModel = registerSetting(new BooleanSetting(
            "show_gpu_model", "Show GPU Name", "Display abbreviated GPU renderer model beside utilization", false
    ));

    public GpuUsageHud() {
        super(
                "gpu_usage_hud",
                "GPU Usage HUD",
                "Shows GPU utilization and render budget load.",
                ModuleCategory.PERFORMANCE_FPS,
                false,
                14.0f,
                186.0f,
                84.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        HardwareTelemetry hw = HardwareTelemetry.getInstance();
        hw.pollMetrics();
        int pct = (int) Math.round(hw.getEstimatedGpuLoadPercent());
        if (showGpuModel.isEnabled()) {
            String gpu = hw.getGpuRenderer();
            if (gpu.length() > 16) {
                gpu = gpu.substring(0, 16);
            }
            drawSimpleLabelValue(context, "GPU", pct + "% (" + gpu + ")");
        } else {
            drawSimpleLabelValue(context, "GPU", pct + "%");
        }
    }
}
