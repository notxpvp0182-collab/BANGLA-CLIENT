package com.banglaclient.module.impl.hud;

import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 140: FPS Display (HUD / Information Category entry)
 * Provides configurable FPS HUD visibility within the HUD / Information category.
 */
public final class FpsHudDisplay extends HudModule {

    private final BooleanSetting showFrameMs = registerSetting(new BooleanSetting(
            "show_frame_ms", "Show Frame Time (ms)", "Append frame render time in milliseconds beside FPS", true
    ));

    public FpsHudDisplay() {
        super(
                "fps_hud_display",
                "FPS HUD",
                "Provides configurable FPS & frame-time HUD visibility.",
                ModuleCategory.HUD_INFORMATION,
                false,
                320.0f,
                278.0f,
                108.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        PerformanceTracker perf = PerformanceTracker.getInstance();
        int fps = perf.getCurrentFps();
        String text = showFrameMs.isEnabled()
                ? String.format("%d FPS (%.1fms)", fps, perf.getCurrentFrameTimeMs())
                : (fps + " FPS");
        drawSimpleLabelValue(context, "", text);
    }
}
