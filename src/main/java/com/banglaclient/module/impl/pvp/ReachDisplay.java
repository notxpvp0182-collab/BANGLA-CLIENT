package com.banglaclient.module.impl.pvp;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.IntegerSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

/**
 * Module 110: Reach Display
 * Displays the measured Euclidean distance of your last legitimate attack hit.
 *
 * FAIR-PLAY GUARANTEE: Strictly read-only measurement; NEVER modifies or extends player reach.
 */
public final class ReachDisplay extends HudModule {

    private final IntegerSetting decimals = registerSetting(new IntegerSetting(
            "reach_decimals", "Decimal Places", "Number of decimal digits to display", 2, 1, 3, 1, ""
    ));

    private double lastHitDistance = 0.0;
    private long lastHitTimestampMillis = 0L;

    public ReachDisplay() {
        super(
                "reach_display",
                "Reach Display",
                "Shows your last hit distance.",
                ModuleCategory.PVP_COMBAT,
                false,
                14.0f,
                532.0f,
                84.0f,
                20.0f
        );
    }

    public void recordAttackOnTarget(Entity target) {
        if (mc.player == null || target == null) {
            return;
        }
        Vec3d eyePos = mc.player.getCameraPosVec(1.0f);
        if (mc.crosshairTarget instanceof EntityHitResult ehr && ehr.getEntity() == target) {
            this.lastHitDistance = eyePos.distanceTo(ehr.getPos());
        } else {
            this.lastHitDistance = eyePos.distanceTo(target.getBoundingBox().getCenter());
        }
        this.lastHitTimestampMillis = System.currentTimeMillis();
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        long now = System.currentTimeMillis();
        boolean recent = (now - lastHitTimestampMillis) <= 4000L;
        String format = "%." + decimals.getValue() + "f blocks";
        String text = recent ? String.format(format, lastHitDistance) : "0.00 blocks";
        drawSimpleLabelValue(context, "", text);
    }
}
