package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 038: Projectile Optimizer
 * Optimizes client-side arrow, trident, snowball, and projectile rendering and ground-stuck arrow culling.
 */
public final class ProjectileOptimizer extends AbstractModule {

    private final BooleanSetting cullGroundedArrows = registerSetting(new BooleanSetting(
            "cull_grounded_arrows", "Cull Distant Ground Arrows", "Hide arrows stuck in blocks beyond threshold distance", true
    ));
    private final IntegerSetting groundedArrowDistance = registerSetting(new IntegerSetting(
            "grounded_arrow_dist", "Grounded Arrow Distance", "Distance beyond which stuck arrows are culled", 20, 8, 64, 4, "m"
    ));

    public ProjectileOptimizer() {
        super(
                "projectile_optimizer",
                "Projectile Optimizer",
                "Optimizes client-side arrow, pearl, and projectile rendering.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean shouldCullGroundedArrow(double squaredDistance) {
        if (!isEnabled() || !cullGroundedArrows.isEnabled()) {
            return false;
        }
        int d = groundedArrowDistance.getValue();
        return squaredDistance > (double) (d * d);
    }
}
