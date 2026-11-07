package com.banglaclient.module.impl.performance;

import com.banglaclient.core.HardwareTelemetry;
import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 060: Performance Profiler
 * Displays a compact multi-line subsystem timing breakdown (Render, Tick, Memory, Entities).
 */
public final class PerformanceProfiler extends HudModule {

    public PerformanceProfiler() {
        super(
                "performance_profiler",
                "Performance Profiler",
                "Shows which client systems consume significant processing time.",
                ModuleCategory.PERFORMANCE_FPS,
                false,
                14.0f,
                332.0f,
                138.0f,
                52.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.textRenderer == null) return;
        this.unscaledWidth = 138.0f;
        this.unscaledHeight = 52.0f;

        PerformanceTracker perf = PerformanceTracker.getInstance();
        HardwareTelemetry hw = HardwareTelemetry.getInstance();
        hw.pollMetrics();

        float totalMs = perf.getCurrentFrameTimeMs();
        int entities = mc.world != null ? mc.world.getRegularEntityCount() : 0;
        float entityShare = Math.min(65.0f, entities * 0.18f);
        float worldShare = Math.max(20.0f, 85.0f - entityShare);

        int col = hudStyle.getTextColor().getArgb();
        boolean sh = hudStyle.isTextShadow();

        context.drawText(mc.textRenderer, String.format("Frame: %.2fms (%d FPS)", totalMs, perf.getCurrentFps()), 6, 5, col, sh);
        context.drawText(mc.textRenderer, String.format("World Pass: ~%.0f%%", worldShare), 6, 17, col, sh);
        context.drawText(mc.textRenderer, String.format("Entity Pass: ~%.0f%% (%d)", entityShare, entities), 6, 29, col, sh);
        context.drawText(mc.textRenderer, String.format("Heap GC: %d%% Used", hw.getMemoryUsagePercent()), 6, 41, col, sh);
    }
}
