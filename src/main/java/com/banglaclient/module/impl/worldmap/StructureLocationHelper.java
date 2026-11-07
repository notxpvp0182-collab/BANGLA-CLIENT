package com.banglaclient.module.impl.worldmap;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 254: Structure Location Helper
 * Displays legitimate Nether Fortress / Bastion quadrant grid sector hints and Stronghold ring
 * distance bands based on the player's current coordinates.
 */
public final class StructureLocationHelper extends HudModule {

    public StructureLocationHelper() {
        super(
                "structure_location_helper",
                "Structure Location Helper",
                "Displays Stronghold ring and Nether quadrant grid info.",
                ModuleCategory.WORLD_MAP,
                false,
                460.0f,
                238.0f,
                156.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.player == null || mc.world == null) {
            drawSimpleLabelValue(context, "Grid", "Quadrant [0, 0]");
            return;
        }
        String dim = mc.world.getRegistryKey().getValue().getPath();
        int bx = mc.player.getBlockX();
        int bz = mc.player.getBlockZ();

        if (dim.contains("the_nether")) {
            // Nether Fortress/Bastion 432x432 region grid
            int regX = Math.floorDiv(bx, 432);
            int regZ = Math.floorDiv(bz, 432);
            drawSimpleLabelValue(context, "Nether Region", regX + ", " + regZ);
        } else {
            double distFromOrigin = Math.hypot(bx, bz);
            String ring = (distFromOrigin >= 1280 && distFromOrigin <= 2816) ? "In Ring 1" : String.format("%.0fm to Origin", distFromOrigin);
            drawSimpleLabelValue(context, "Stronghold", ring);
        }
    }
}
