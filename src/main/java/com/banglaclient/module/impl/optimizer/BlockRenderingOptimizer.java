package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 005: Block Rendering Optimizer
 * Optimizes block rendering paths and avoids redundant foliage/breaking overlay work.
 */
public final class BlockRenderingOptimizer extends AbstractModule {

    private final BooleanSetting fastFoliage = registerSetting(new BooleanSetting(
            "fast_foliage", "Fast Foliage Culling", "Reduce interior face overdraw on dense leaves", true
    ));
    private final BooleanSetting skipDistantBreakingOverlay = registerSetting(new BooleanSetting(
            "skip_distant_cracks", "Cull Distant Block Cracks", "Skip block breaking crack overlays beyond 24 blocks", true
    ));

    public BlockRenderingOptimizer() {
        super(
                "block_rendering_optimizer",
                "Block Rendering Optimizer",
                "Optimizes block rendering paths and reduces foliage overdraw.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean isFastFoliage() {
        return isEnabled() && fastFoliage.isEnabled();
    }

    public boolean shouldSkipBreakingOverlay(double squaredDistance) {
        return isEnabled() && skipDistantBreakingOverlay.isEnabled() && squaredDistance > 576.0;
    }
}
