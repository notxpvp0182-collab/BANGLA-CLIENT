package com.banglaclient.module.impl.hud;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

/**
 * Module 133: Chunk Coordinates
 * Displays current chunk X/Z coordinates and optionally intra-chunk relative block coordinates (0..15).
 */
public final class ChunkCoordinates extends HudModule {

    private final BooleanSetting showSubChunkOffset = registerSetting(new BooleanSetting(
            "show_subchunk_offset", "Show Local (0-15) Offset", "Display block offset within the current 16x16 chunk", true
    ));

    public ChunkCoordinates() {
        super(
                "chunk_coordinates",
                "Chunk Coordinates",
                "Displays current chunk X/Z and local offset.",
                ModuleCategory.HUD_INFORMATION,
                false,
                320.0f,
                88.0f,
                126.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.player == null) {
            drawSimpleLabelValue(context, "Chunk", "0, 0 [8, 8]");
            return;
        }
        BlockPos bPos = mc.player.getBlockPos();
        ChunkPos cPos = mc.player.getChunkPos();
        if (showSubChunkOffset.isEnabled()) {
            int localX = bPos.getX() & 15;
            int localZ = bPos.getZ() & 15;
            drawSimpleLabelValue(context, "Chunk", cPos.x + ", " + cPos.z + " [" + localX + ", " + localZ + "]");
        } else {
            drawSimpleLabelValue(context, "Chunk", cPos.x + ", " + cPos.z);
        }
    }
}
