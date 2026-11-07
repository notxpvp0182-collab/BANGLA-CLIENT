package com.banglaclient.module.impl.advanced;

import com.banglaclient.core.HardwareTelemetry;
import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 281: Performance Dashboard
 * Centralizes FPS, 1% Low, CPU, GPU, RAM, chunks, and entity telemetry into a unified HUD card.
 */
public final class PerformanceDashboard extends HudModule {

    public PerformanceDashboard() {
        super(
                "performance_dashboard",
                "Performance Dashboard",
                "Centralizes FPS, CPU, GPU, RAM, chunks, and entity info.",
                ModuleCategory.ADVANCED_PERFORMANCE,
                false,
                460.0f,
                342.0f,
                158.0f,
                54.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.textRenderer == null) return;
        this.unscaledWidth = 158.0f;
        this.unscaledHeight = 54.0f;

        PerformanceTracker perf = PerformanceTracker.getInstance();
        HardwareTelemetry hw = HardwareTelemetry.getInstance();
        hw.pollMetrics();

        int entities = mc.world != null ? mc.world.getRegularEntityCount() : 0;
        int chunks = (mc.world != null && mc.world.getChunkManager() != null) ? mc.world.getChunkManager().getLoadedChunkCount() : 0;

        int col = hudStyle.getTextColor().getArgb();
        boolean sh = hudStyle.isTextShadow();

        context.drawText(mc.textRenderer, String.format("FPS: %d | 1%% Low: %d", perf.getCurrentFps(), perf.getOnePercentLowFps()), 6, 5, col, sh);
        context.drawText(mc.textRenderer, String.format("CPU: %.0f%% | Est. GPU: %.0f%%", hw.getSystemCpuLoadPercent(), hw.getEstimatedGpuLoadPercent()), 6, 17, col, sh);
        context.drawText(mc.textRenderer, String.format("RAM: %d/%d MB (%d%%)", hw.getUsedMemoryMb(), hw.getMaxMemoryMb(), hw.getMemoryUsagePercent()), 6, 29, col, sh);
        context.drawText(mc.textRenderer, String.format("Ent: %d | Chunks: %d", entities, chunks), 6, 41, col, sh);
    }
}
