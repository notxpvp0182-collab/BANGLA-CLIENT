package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 013: Chunk Update Batching
 * Processes compatible neighbor block/light chunk updates in coalesced batches per tick
 * instead of triggering immediate repeated section rebuilds.
 */
public final class ChunkUpdateBatching extends AbstractModule {

    private final IntegerSetting batchWindowTicks = registerSetting(new IntegerSetting(
            "batch_window_ticks", "Coalesce Window", "Group rapid block updates within this tick window", 2, 1, 5, 1, " ticks"
    ));
    private final BooleanSetting deferDistantRebuild = registerSetting(new BooleanSetting(
            "defer_distant_rebuild", "Defer Distant Sections", "Batch non-adjacent chunk section updates together", true
    ));

    public ChunkUpdateBatching() {
        super(
                "chunk_update_batching",
                "Chunk Update Batching",
                "Batches compatible chunk updates to prevent repeated rebuild work.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public int getBatchWindowTicks() {
        return isEnabled() ? batchWindowTicks.getValue() : 1;
    }

    public boolean isDeferDistantRebuild() {
        return isEnabled() && deferDistantRebuild.isEnabled();
    }
}
