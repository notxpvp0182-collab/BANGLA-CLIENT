package com.banglaclient.module.impl.worldmap;

import com.banglaclient.keybind.Keybind;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import org.lwjgl.glfw.GLFW;

/**
 * Module 251: Chunk Border Display
 * Displays 16x16 chunk boundary distances and current chunk section bounds.
 */
public final class ChunkBorderDisplay extends HudModule {

    public ChunkBorderDisplay() {
        super(
                "chunk_border_display",
                "Chunk Border Display",
                "Displays 16x16 chunk boundaries and edge offsets.",
                ModuleCategory.WORLD_MAP,
                false,
                460.0f,
                262.0f,
                152.0f,
                20.0f
        );
        registerKeybind(Keybind.ofKey(GLFW.GLFW_KEY_F9));
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        if (mc.player == null) {
            drawSimpleLabelValue(context, "Chunk Bounds", "[0..15, 0..15]");
            return;
        }
        ChunkPos cp = mc.player.getChunkPos();
        BlockPos bp = mc.player.getBlockPos();
        int relX = bp.getX() & 15;
        int relZ = bp.getZ() & 15;
        String text = String.format("[%d..%d, %d..%d] (%d,%d)", cp.getStartX(), cp.getEndX(), cp.getStartZ(), cp.getEndZ(), relX, relZ);
        drawSimpleLabelValue(context, "Border", text);
    }
}
