package com.banglaclient.module.impl.pvp;

import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 105: Left CPS / Right CPS
 * Separately tracks and renders dual split boxes for LMB CPS and RMB CPS.
 */
public final class LeftRightCps extends HudModule {

    public LeftRightCps() {
        super(
                "left_right_cps",
                "Left CPS / Right CPS",
                "Separately tracks left and right mouse button CPS.",
                ModuleCategory.PVP_COMBAT,
                false,
                14.0f,
                460.0f,
                96.0f,
                22.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.textRenderer == null) return;
        this.unscaledWidth = 96.0f;
        this.unscaledHeight = 22.0f;

        PerformanceTracker perf = PerformanceTracker.getInstance();
        String lText = "LMB: " + perf.getLeftCps();
        String rText = "RMB: " + perf.getRightCps();

        int col = hudStyle.getTextColor().getArgb();
        boolean sh = hudStyle.isTextShadow();

        context.drawText(mc.textRenderer, lText, 6, 7, col, sh);
        Render2DUtil.drawRect(context, 47.0f, 4.0f, 1.0f, 14.0f, hudStyle.getOutlineColor().getArgb());
        context.drawText(mc.textRenderer, rText, 53, 7, col, sh);
    }
}
