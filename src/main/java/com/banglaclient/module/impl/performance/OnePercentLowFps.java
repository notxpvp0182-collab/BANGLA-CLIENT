package com.banglaclient.module.impl.performance;

import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 050: 1% Low FPS
 * Tracks and displays the worst 1% frame performance over a rolling sample window.
 */
public final class OnePercentLowFps extends HudModule {

    private final BooleanSetting showStabilityBadge = registerSetting(new BooleanSetting(
            "show_stability_badge", "Show Stability Ratio", "Show percentage ratio of 1% Low to Average FPS", true
    ));

    public OnePercentLowFps() {
        super(
                "one_percent_low_fps",
                "1% Low FPS",
                "Tracks the worst 1% frame performance over a rolling sample.",
                ModuleCategory.PERFORMANCE_FPS,
                false,
                14.0f,
                38.0f,
                92.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        PerformanceTracker perf = PerformanceTracker.getInstance();
        int low = perf.getOnePercentLowFps();
        int avg = Math.max(1, perf.getCurrentFps());
        if (showStabilityBadge.isEnabled()) {
            int ratio = Math.min(100, Math.round((low * 100.0f) / avg));
            drawSimpleLabelValue(context, "1% Low", low + " FPS (" + ratio + "%)");
        } else {
            drawSimpleLabelValue(context, "1% Low", low + " FPS");
        }
    }
}
