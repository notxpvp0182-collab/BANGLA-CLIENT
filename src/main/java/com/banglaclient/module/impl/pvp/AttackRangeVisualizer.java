package com.banglaclient.module.impl.pvp;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.ColorSetting;
import com.banglaclient.theme.ColorData;
import com.banglaclient.util.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.hit.EntityHitResult;

/**
 * Module 121: Attack Range Visualizer
 * Visually displays live target distance vs vanilla 3.00m survival attack range on a gauge
 * without modifying or extending actual reach.
 */
public final class AttackRangeVisualizer extends HudModule {

    private final ColorSetting inRangeColor = registerSetting(new ColorSetting(
            "in_range_color", "In-Range Color", "Bar color when target is within legitimate 3.0m reach", ColorData.RED_ACCENT, true
    ));
    private final BooleanSetting showNumericMeters = registerSetting(new BooleanSetting(
            "show_numeric_meters", "Show Exact Meters", "Display live crosshair entity distance in meters", true
    ));

    public AttackRangeVisualizer() {
        super(
                "attack_range_visualizer",
                "Attack Range Visualizer",
                "Visually displays live attack range measurement without altering reach.",
                ModuleCategory.PVP_COMBAT,
                false,
                220.0f,
                412.0f,
                112.0f,
                24.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        this.unscaledWidth = 112.0f;
        this.unscaledHeight = 24.0f;

        double dist = 0.0;
        boolean hasTarget = false;
        if (mc.player != null && mc.crosshairTarget instanceof EntityHitResult ehr) {
            dist = mc.player.getCameraPosVec(tickDelta).distanceTo(ehr.getPos());
            hasTarget = true;
        }

        if (mc.textRenderer != null) {
            String label = hasTarget
                    ? (showNumericMeters.isEnabled() ? String.format("Range: %.2fm / 3.00m", dist) : "Target In Sight")
                    : "Range: -- / 3.00m";
            context.drawText(mc.textRenderer, label, 5, 4, hudStyle.getTextColor().getArgb(), hudStyle.isTextShadow());
        }

        float barW = unscaledWidth - 10.0f;
        Render2DUtil.drawRoundedRect(context, 5.0f, 15.0f, barW, 4.0f, 2.0f, 0xFF181820);
        if (hasTarget) {
            float ratio = (float) Math.min(1.0, Math.max(0.0, (3.0 - Math.min(3.0, dist)) / 3.0));
            Render2DUtil.drawRoundedRect(context, 5.0f, 15.0f, Math.max(4.0f, barW * ratio), 4.0f, 2.0f,
                    inRangeColor.getValue().withAlphaFactor(hudStyle.getOpacity()).getArgb());
        }
    }
}
