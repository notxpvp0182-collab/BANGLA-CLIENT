package com.banglaclient.module.impl.hud;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.hit.HitResult;

/**
 * Module 134: Distance Display
 * Displays exact raycast distance in meters to the block or entity currently under the crosshair.
 */
public final class DistanceDisplay extends HudModule {

    public DistanceDisplay() {
        super(
                "distance_display",
                "Distance Display",
                "Displays distance to the targeted block or entity.",
                ModuleCategory.HUD_INFORMATION,
                false,
                320.0f,
                112.0f,
                94.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.player != null && mc.crosshairTarget != null && mc.crosshairTarget.getType() != HitResult.Type.MISS) {
            double dist = mc.player.getCameraPosVec(tickDelta).distanceTo(mc.crosshairTarget.getPos());
            drawSimpleLabelValue(context, "Dist", String.format("%.2fm", dist));
        } else {
            drawSimpleLabelValue(context, "Dist", "--");
        }
    }
}
