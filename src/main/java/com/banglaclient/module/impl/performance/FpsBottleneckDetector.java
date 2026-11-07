package com.banglaclient.module.impl.performance;

import com.banglaclient.core.HardwareTelemetry;
import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 061: FPS Bottleneck Detector
 * Analyzes frame-time patterns, JVM heap pressure, CPU load, and world entity density
 * to identify and display the primary performance bottleneck in real time.
 */
public final class FpsBottleneckDetector extends HudModule {

    public FpsBottleneckDetector() {
        super(
                "fps_bottleneck_detector",
                "FPS Bottleneck Detector",
                "Analyzes frame-time patterns and suggests likely bottlenecks.",
                ModuleCategory.PERFORMANCE_FPS,
                false,
                14.0f,
                388.0f,
                148.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        HardwareTelemetry hw = HardwareTelemetry.getInstance();
        PerformanceTracker perf = PerformanceTracker.getInstance();
        hw.pollMetrics();

        String diagnosis = "Balanced (Optimal)";
        int entities = (mc.world != null) ? mc.world.getRegularEntityCount() : 0;

        if (hw.getMemoryUsagePercent() >= 88) {
            diagnosis = "JVM Heap / GC Pressure";
        } else if (entities > 250) {
            diagnosis = "High Entity Count (" + entities + ")";
        } else if (hw.getSystemCpuLoadPercent() >= 85.0) {
            diagnosis = "CPU Thread Bound";
        } else if (perf.getCurrentFrameTimeMs() > 22.0f) {
            diagnosis = "GPU / Shader Fillrate";
        }

        drawSimpleLabelValue(context, "Bottleneck", diagnosis);
    }
}
