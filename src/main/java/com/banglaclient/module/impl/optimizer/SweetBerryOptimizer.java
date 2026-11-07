package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 045: Sweet Berry Optimizer
 * Optimizes sweet berry bush cross-quad cutout rendering and movement slowness FOV jitter.
 */
public final class SweetBerryOptimizer extends AbstractModule {

    private final BooleanSetting fastCutoutPass = registerSetting(new BooleanSetting(
            "fast_berry_cutout", "Fast Bush Cutout", "Reduce alpha overdraw on dense sweet berry bush patches", true
    ));
    private final BooleanSetting stabilizeBushFov = registerSetting(new BooleanSetting(
            "stabilize_bush_fov", "Stabilize Bush FOV", "Prevent rapid camera FOV snapping when walking through berry bushes", true
    ));

    public SweetBerryOptimizer() {
        super(
                "sweet_berry_optimizer",
                "Sweet Berry Optimizer",
                "Optimizes sweet berry bush rendering and interaction visuals.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean isFastCutoutPass() {
        return isEnabled() && fastCutoutPass.isEnabled();
    }

    public boolean isStabilizeBushFov() {
        return isEnabled() && stabilizeBushFov.isEnabled();
    }
}
