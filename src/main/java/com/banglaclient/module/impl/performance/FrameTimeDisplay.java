package com.banglaclient.module.impl.performance;

import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 051: Frame Time Display
 * Shows exact frame render time in milliseconds on a free-floating HUD element.
 */
public final class FrameTimeDisplay extends HudModule {

    private final BooleanSetting showBudgetStatus = registerSetting(new BooleanSetting(
            "show_budget_status", "Show 60Hz Budget", "Indicate whether frame time is within 16.67ms budget", false
    ));

    public FrameTimeDisplay() {
        super(
                "frame_time_display",
                "Frame Time Display",
                "Shows frame render time in milliseconds.",
                ModuleCategory.PERFORMANCE_FPS,
                false,
                14.0f,
                62.0f,
                88.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        float ms = PerformanceTracker.getInstance().getCurrentFrameTimeMs();
        String val = String.format("%.2f ms", ms);
        if (showBudgetStatus.isEnabled()) {
            val += (ms <= 16.67f ? " (OK)" : " (HIGH)");
        }
        drawSimpleLabelValue(context, "Frame", val);
    }
}
