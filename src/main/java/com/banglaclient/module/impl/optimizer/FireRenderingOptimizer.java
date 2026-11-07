package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 026: Fire Rendering Optimizer
 * Optimizes world fire block smoke particles and entity flame overlays at distance.
 */
public final class FireRenderingOptimizer extends AbstractModule {

    private final BooleanSetting reduceFireSmoke = registerSetting(new BooleanSetting(
            "reduce_fire_smoke", "Reduce Fire Smoke", "Limit large smoke particles emitted by world fire blocks", true
    ));
    private final IntegerSetting entityFlameCullDist = registerSetting(new IntegerSetting(
            "entity_flame_cull_dist", "Entity Flame Distance", "Skip multi-quad burning flame overlay on entities beyond distance", 28, 8, 64, 4, "m"
    ));

    public FireRenderingOptimizer() {
        super(
                "fire_rendering_optimizer",
                "Fire Rendering Optimizer",
                "Optimizes fire overlay and burning entity rendering workload.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean isReduceFireSmoke() {
        return isEnabled() && reduceFireSmoke.isEnabled();
    }

    public boolean shouldCullEntityFlame(double squaredDistance) {
        if (!isEnabled()) {
            return false;
        }
        int d = entityFlameCullDist.getValue();
        return squaredDistance > (double) (d * d);
    }
}
