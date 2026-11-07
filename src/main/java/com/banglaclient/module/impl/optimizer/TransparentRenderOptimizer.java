package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 017: Transparent Render Optimizer
 * Optimizes translucent geometry sorting (such as stained glass and water) by skipping redundant
 * per-frame translucent quad resorts when camera movement is negligible.
 */
public final class TransparentRenderOptimizer extends AbstractModule {

    private final FloatSetting sortThresholdDistance = registerSetting(new FloatSetting(
            "sort_threshold_dist", "Sort Movement Threshold", "Minimum camera movement before resorting translucent quads", 1.25f, 0.25f, 4.0f, 0.25f, "m"
    ));
    private final BooleanSetting skipDistantTranslucentSort = registerSetting(new BooleanSetting(
            "skip_distant_sort", "Skip Distant Quad Sort", "Avoid resorting translucent chunks beyond 64 blocks", true
    ));

    public TransparentRenderOptimizer() {
        super(
                "transparent_render_optimizer",
                "Transparent Render Optimizer",
                "Optimizes transparent block sorting for glass and water.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public float getSortThresholdSquared() {
        float d = sortThresholdDistance.getValue();
        return d * d;
    }

    public boolean shouldSkipDistantSort(double squaredChunkDistance) {
        return isEnabled() && skipDistantTranslucentSort.isEnabled() && squaredChunkDistance > 4096.0;
    }
}
