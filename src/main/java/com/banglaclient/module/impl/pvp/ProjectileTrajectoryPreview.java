package com.banglaclient.module.impl.pvp;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.item.WindChargeItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

/**
 * Module 122: Projectile Trajectory Preview
 * Calculates and displays legitimate client-side ballistic flight distance and estimated impact
 * coordinates when holding a Bow, Crossbow, Ender Pearl, Trident, or Wind Charge.
 */
public final class ProjectileTrajectoryPreview extends HudModule {

    private final BooleanSetting showImpactDistance = registerSetting(new BooleanSetting(
            "show_impact_dist", "Show Landing Distance", "Display estimated landing distance in blocks", true
    ));

    public ProjectileTrajectoryPreview() {
        super(
                "projectile_trajectory_preview",
                "Projectile Trajectory Preview",
                "Previews projectile trajectory using client-side physics.",
                ModuleCategory.PVP_COMBAT,
                false,
                220.0f,
                440.0f,
                128.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.player == null || mc.world == null) {
            drawSimpleLabelValue(context, "Trajectory", "Idle");
            return;
        }
        ItemStack stack = mc.player.getMainHandStack();
        Item item = stack.getItem();
        boolean isProjectile = item instanceof BowItem
                || item instanceof CrossbowItem
                || item instanceof EnderPearlItem
                || item instanceof TridentItem
                || item instanceof WindChargeItem;

        if (!isProjectile) {
            drawSimpleLabelValue(context, "Trajectory", "No Projectile Held");
            return;
        }

        double speed = (item instanceof BowItem || item instanceof CrossbowItem) ? 3.0 : 1.5;
        double gravity = (item instanceof WindChargeItem) ? 0.0 : 0.03;

        Vec3d pos = mc.player.getCameraPosVec(tickDelta);
        Vec3d vel = mc.player.getRotationVec(tickDelta).multiply(speed);

        for (int step = 0; step < 60; step++) {
            Vec3d nextPos = pos.add(vel);
            BlockHitResult hit = mc.world.raycast(new RaycastContext(
                    pos, nextPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player
            ));
            if (hit.getType() != HitResult.Type.MISS) {
                pos = hit.getPos();
                break;
            }
            pos = nextPos;
            vel = vel.multiply(0.99).subtract(0.0, gravity, 0.0);
        }

        double impactDist = mc.player.getPos().distanceTo(pos);
        String text = showImpactDistance.isEnabled()
                ? String.format("Landing: ~%.1fm", impactDist)
                : String.format("X:%d Y:%d Z:%d", (int) pos.x, (int) pos.y, (int) pos.z);
        drawSimpleLabelValue(context, "", text);
    }
}
