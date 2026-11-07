package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 014: Chunk Mesh Cache
 * Caches reusable chunk-rendering state and vertex buffer allocations where technically safe.
 */
public final class ChunkMeshCache extends AbstractModule {

    private final IntegerSetting maxCachedSections = registerSetting(new IntegerSetting(
            "max_cached_sections", "Cache Pool Size", "Maximum pooled chunk section buffers kept in memory", 256, 64, 1024, 32, ""
    ));
    private final BooleanSetting reuseEmptySectionState = registerSetting(new BooleanSetting(
            "reuse_empty_sections", "Fast Empty Section Skip", "Immediately skip air-only chunk sections without buffer lookup", true
    ));

    public ChunkMeshCache() {
        super(
                "chunk_mesh_cache",
                "Chunk Mesh Cache",
                "Caches reusable chunk-rendering data and skips empty sections.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public int getMaxCachedSections() {
        return maxCachedSections.getValue();
    }

    public boolean isReuseEmptySectionState() {
        return isEnabled() && reuseEmptySectionState.isEnabled();
    }
}
