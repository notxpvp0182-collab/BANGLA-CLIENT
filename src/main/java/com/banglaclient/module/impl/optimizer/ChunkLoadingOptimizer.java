package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 010: Chunk Loading Optimizer
 * Optimizes client chunk loading and update handling to prevent frame-time stutters during movement.
 */
public final class ChunkLoadingOptimizer extends AbstractModule {

    private final IntegerSetting maxBuildsPerFrame = registerSetting(new IntegerSetting(
            "max_builds_per_frame", "Max Uploads Per Frame", "Limit chunk mesh uploads per render frame to prevent stutters", 3, 1, 8, 1, ""
    ));
    private final BooleanSetting prioritizeViewFrustum = registerSetting(new BooleanSetting(
            "prioritize_frustum", "Prioritize Camera Cone", "Build chunks directly in front of the player first", true
    ));

    public ChunkLoadingOptimizer() {
        super(
                "chunk_loading_optimizer",
                "Chunk Loading Optimizer",
                "Optimizes client chunk loading and mesh upload pacing.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public int getMaxBuildsPerFrame() {
        return isEnabled() ? maxBuildsPerFrame.getValue() : 8;
    }

    public boolean isPrioritizeViewFrustum() {
        return isEnabled() && prioritizeViewFrustum.isEnabled();
    }
}
