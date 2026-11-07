package com.banglaclient.module.impl.advanced;

import com.banglaclient.core.ClientCore;
import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 289: Session Statistics
 * Tracks and displays session duration, peak FPS, average FPS, and 1% low FPS on a HUD card.
 */
public final class SessionStatistics extends HudModule {

    private int peakFps = 0;

    public SessionStatistics() {
        super(
                "session_statistics",
                "Session Statistics",
                "Shows current session performance statistics.",
                ModuleCategory.ADVANCED_PERFORMANCE,
                false,
                460.0f,
                402.0f,
                148.0f,
                42.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.textRenderer == null) return;
        this.unscaledWidth = 148.0f;
        this.unscaledHeight = 42.0f;

        PerformanceTracker perf = PerformanceTracker.getInstance();
        int cur = perf.getCurrentFps();
        if (cur > peakFps) {
            peakFps = cur;
        }
        long mins = ClientCore.getInstance().getSessionDurationSeconds() / 60L;

        int col = hudStyle.getTextColor().getArgb();
        boolean sh = hudStyle.isTextShadow();

        context.drawText(mc.textRenderer, "Session Time: " + mins + " min", 6, 5, col, sh);
        context.drawText(mc.textRenderer, "Peak FPS: " + peakFps + " FPS", 6, 17, col, sh);
        context.drawText(mc.textRenderer, "1% Low: " + perf.getOnePercentLowFps() + " FPS", 6, 29, col, sh);
    }
}
