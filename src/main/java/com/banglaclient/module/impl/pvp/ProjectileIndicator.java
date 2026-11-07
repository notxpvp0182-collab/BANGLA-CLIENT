package com.banglaclient.module.impl.pvp;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;

/**
 * Module 118: Projectile Indicator
 * Displays remaining arrows, ender pearls, and wind charges available in inventory.
 */
public final class ProjectileIndicator extends HudModule {

    public ProjectileIndicator() {
        super(
                "projectile_indicator",
                "Projectile Indicator",
                "Displays available arrows, pearls, and wind charges.",
                ModuleCategory.PVP_COMBAT,
                false,
                220.0f,
                340.0f,
                122.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        int arrows = 0;
        int pearls = 0;
        int wind = 0;

        if (mc.player != null) {
            PlayerInventory inv = mc.player.getInventory();
            for (int i = 0; i < inv.size(); i++) {
                var stack = inv.getStack(i);
                if (stack.isOf(Items.ARROW) || stack.isOf(Items.TIPPED_ARROW) || stack.isOf(Items.SPECTRAL_ARROW)) {
                    arrows += stack.getCount();
                } else if (stack.isOf(Items.ENDER_PEARL)) {
                    pearls += stack.getCount();
                } else if (stack.isOf(Items.WIND_CHARGE)) {
                    wind += stack.getCount();
                }
            }
        }
        drawSimpleLabelValue(context, "", "Arr: " + arrows + " | Prl: " + pearls + " | Wnd: " + wind);
    }
}
