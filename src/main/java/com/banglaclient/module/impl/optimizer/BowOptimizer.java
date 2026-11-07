package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 037: Bow Optimizer
 * Optimizes bow charging animation interpolation and critical-arrow particle trails.
 */
public final class BowOptimizer extends AbstractModule {

    private final BooleanSetting smoothBowPull = registerSetting(new BooleanSetting(
            "smooth_bow_pull", "Smooth Draw Animation", "Eliminate micro-jitter at full bow tension", true
    ));
    private final BooleanSetting reduceCritArrowParticles = registerSetting(new BooleanSetting(
            "reduce_crit_particles", "Reduce Crit Trail Clutter", "Half the critical-hit particle density on flying arrows for clearer aim", true
    ));

    public BowOptimizer() {
        super(
                "bow_optimizer",
                "Bow Optimizer",
                "Optimizes bow charging animations and arrow trail clarity.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean isSmoothBowPull() {
        return isEnabled() && smoothBowPull.isEnabled();
    }

    public boolean isReduceCritArrowParticles() {
        return isEnabled() && reduceCritArrowParticles.isEnabled();
    }
}
