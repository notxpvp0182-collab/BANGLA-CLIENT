package com.banglaclient.module.impl.pvp;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;

/**
 * Module 120: Crystal Status HUD
 * Displays your inventory End Crystal + Obsidian count and alerts if a live End Crystal is within blast range.
 */
public final class CrystalStatusHud extends HudModule {

    public CrystalStatusHud() {
        super(
                "crystal_status_hud",
                "Crystal Status HUD",
                "Displays End Crystal inventory count and nearby crystal danger.",
                ModuleCategory.PVP_COMBAT,
                false,
                220.0f,
                388.0f,
                126.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        int crystals = 0;
        int obsidian = 0;
        int nearbyActive = 0;

        if (mc.player != null) {
            PlayerInventory inv = mc.player.getInventory();
            for (int i = 0; i < inv.size(); i++) {
                var stack = inv.getStack(i);
                if (stack.isOf(Items.END_CRYSTAL)) {
                    crystals += stack.getCount();
                } else if (stack.isOf(Items.OBSIDIAN)) {
                    obsidian += stack.getCount();
                }
            }
            if (mc.world != null) {
                for (Entity e : mc.world.getEntities()) {
                    if (e instanceof EndCrystalEntity && mc.player.squaredDistanceTo(e) <= 144.0) {
                        nearbyActive++;
                    }
                }
            }
        }
        String text = "Crys: " + crystals + " | Obs: " + obsidian + (nearbyActive > 0 ? (" [" + nearbyActive + " LIVE]") : "");
        drawSimpleLabelValue(context, "", text);
    }
}
