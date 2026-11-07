package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 033: Elytra Optimizer
 * Optimizes Elytra flight rendering, firework rocket trail density, and wing model interpolation for distant players.
 */
public final class ElytraOptimizer extends AbstractModule {

    private final BooleanSetting reduceFireworkTrails = registerSetting(new BooleanSetting(
            "reduce_rocket_trails", "Reduce Rocket Trail Lag", "Throttle firework spark trails during high-speed Elytra flight", true
    ));
    private final IntegerSetting distantWingSimplifyDist = registerSetting(new IntegerSetting(
            "distant_wing_dist", "Wing Simplify Distance", "Skip per-frame wingcape physics math on players beyond distance", 32, 12, 64, 4, "m"
    ));

    public ElytraOptimizer() {
        super(
                "elytra_optimizer",
                "Elytra Optimizer",
                "Optimizes Elytra flight rendering and firework trail workload.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean isReduceFireworkTrails() {
        return isEnabled() && reduceFireworkTrails.isEnabled();
    }

    public boolean shouldSimplifyDistantWings(double squaredDistance) {
        if (!isEnabled()) {
            return false;
        }
        int d = distantWingSimplifyDist.getValue();
        return squaredDistance > (double) (d * d);
    }
}
