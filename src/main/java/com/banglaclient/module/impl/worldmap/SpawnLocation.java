package com.banglaclient.module.impl.worldmap;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.BlockPos;

/**
 * Module 253: Spawn Location
 * Displays the world compass spawn point coordinates and distance from your current position.
 */
public final class SpawnLocation extends HudModule {

    public SpawnLocation() {
        super(
                "spawn_location",
                "Spawn Location",
                "Displays known world spawn coordinates and distance.",
                ModuleCategory.WORLD_MAP,
                false,
                460.0f,
                214.0f,
                144.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.world == null || mc.player == null) {
            drawSimpleLabelValue(context, "Spawn", "0, 64, 0");
            return;
        }
        BlockPos spawn = mc.world.getSpawnPos();
        double dist = Math.sqrt(mc.player.squaredDistanceTo(spawn.getX(), spawn.getY(), spawn.getZ()));
        String text = String.format("%d, %d, %d (%.0fm)", spawn.getX(), spawn.getY(), spawn.getZ(), dist);
        drawSimpleLabelValue(context, "Spawn", text);
    }
}
