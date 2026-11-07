package com.banglaclient.module.impl.performance;

import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;

/**
 * Module 059: Chunk Counter
 * Shows loaded chunk count in the current client world.
 */
public final class ChunkCounter extends HudModule {

    public ChunkCounter() {
        super(
                "chunk_counter",
                "Chunk Counter",
                "Shows loaded/rendered chunk count.",
                ModuleCategory.PERFORMANCE_FPS,
                false,
                14.0f,
                306.0f,
                88.0f,
                20.0f
        );
    }

    @Override
    protected void renderHudContent(DrawContext context, float tickDelta, boolean isEditing) {
        int loaded = (mc.world != null && mc.world.getChunkManager() != null)
                ? mc.world.getChunkManager().getLoadedChunkCount()
                : 0;
        drawSimpleLabelValue(context, "Chunks", String.valueOf(loaded));
    }
}
