package com.banglaclient.module.impl.performance;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 071: Fast World Loading
 * Optimizes client-side world loading tasks by skipping artificial terrain-ready wait delays
 * once the player's immediate chunk column is received.
 */
public final class FastWorldLoading extends AbstractModule {

    private final BooleanSetting skipSpawnChunkWait = registerSetting(new BooleanSetting(
            "skip_spawn_chunk_wait", "Immediate Terrain Entry", "Enter the world as soon as the player's immediate chunk is built", true
    ));
    private final BooleanSetting clearStaleRenderBuffers = registerSetting(new BooleanSetting(
            "clear_stale_buffers", "Pre-Clear World Buffers", "Release previous world vertex buffers immediately on world transition", true
    ));

    public FastWorldLoading() {
        super(
                "fast_world_loading",
                "Fast World Loading",
                "Optimizes client-side world loading tasks and transition speed.",
                ModuleCategory.PERFORMANCE_FPS,
                true
        );
    }

    public boolean isSkipSpawnChunkWait() {
        return isEnabled() && skipSpawnChunkWait.isEnabled();
    }

    public boolean isClearStaleRenderBuffers() {
        return isEnabled() && clearStaleRenderBuffers.isEnabled();
    }
}
