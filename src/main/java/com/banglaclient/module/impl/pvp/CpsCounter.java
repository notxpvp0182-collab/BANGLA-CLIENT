package com.banglaclient.module.impl.pvp;

import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 104: CPS Counter
 * Tracks clicks per second on a free-floating, non-snapping HUD element.
 */
public final class CpsCounter extends HudModule {

    private final BooleanSetting showRightClickToo = registerSetting(new BooleanSetting(
            "show_right_cps", "Show Right CPS", "Display both Left and Right CPS (e.g., 12 | 8 CPS)", true
    ));

    public CpsCounter() {
        super(
                "cps_counter",
                "CPS Counter",
                "Tracks clicks per second.",
                ModuleCategory.PVP_COMBAT,
                false,
                14.0f,
                436.0f,
                78.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        PerformanceTracker perf = PerformanceTracker.getInstance();
        int left = perf.getLeftCps();
        int right = perf.getRightCps();
        String text = showRightClickToo.isEnabled()
                ? (left + " | " + right + " CPS")
                : (left + " CPS");
        drawSimpleLabelValue(context, "", text);
    }
}
