package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 009: Animation Optimizer
 * Reduces unnecessary client-side limb/block animation processing when off-screen or far away.
 */
public final class AnimationOptimizer extends AbstractModule {

    private final BooleanSetting throttleDistantLimbSwings = registerSetting(new BooleanSetting(
            "throttle_distant_limbs", "Throttle Distant Limb Math", "Skip complex limb trigonometric animations for distant entities", true
    ));
    private final IntegerSetting limbSimpleDistance = registerSetting(new IntegerSetting(
            "limb_simple_dist", "Limb Simplify Distance", "Distance threshold for simplified entity limb animation", 36, 16, 64, 4, "m"
    ));

    public AnimationOptimizer() {
        super(
                "animation_optimizer",
                "Animation Optimizer",
                "Reduces unnecessary client-side animation processing where safe.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean shouldSimplifyLimbAnimation(double squaredDistance) {
        if (!isEnabled() || !throttleDistantLimbSwings.isEnabled()) {
            return false;
        }
        int d = limbSimpleDistance.getValue();
        return squaredDistance > (double) (d * d);
    }
}
