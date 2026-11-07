package com.banglaclient.module.impl.performance;

import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.ModeSetting;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 049: FPS Display (Performance & FPS Category)
 * Shows current frames per second on a free-floating, non-snapping HUD element.
 */
public final class FpsDisplayModule extends HudModule {

    private final ModeSetting formatMode = registerSetting(new ModeSetting(
            "format_mode", "Display Format", "Text format for the FPS counter", "144 FPS", "144 FPS", "FPS: 144", "[144 FPS]"
    ));
    private final BooleanSetting includeMinMax = registerSetting(new BooleanSetting(
            "include_1pct", "Show 1% Low Inline", "Append 1% Low FPS beside current FPS", false
    ));

    public FpsDisplayModule() {
        super(
                "fps_display",
                "FPS Display",
                "Shows your current FPS.",
                ModuleCategory.PERFORMANCE_FPS,
                true,
                14.0f,
                14.0f,
                72.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        int fps = PerformanceTracker.getInstance().getCurrentFps();
        String text = switch (formatMode.getValue()) {
            case "FPS: 144" -> "FPS: " + fps;
            case "[144 FPS]" -> "[" + fps + " FPS]";
            default -> fps + " FPS";
        };
        if (includeMinMax.isEnabled()) {
            text += " (" + PerformanceTracker.getInstance().getOnePercentLowFps() + " 1%)";
        }
        drawSimpleLabelValue(context, "", text);
    }
}
