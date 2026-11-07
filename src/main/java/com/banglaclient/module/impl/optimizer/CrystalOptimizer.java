package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 039: Crystal Optimizer
 * Optimizes End Crystal rendering (inner cube matrix passes, bobbing math, and explosion particle spikes).
 * Note: Strictly client-side visual/rendering optimization; does NOT send illegal attack packets or modify server state.
 */
public final class CrystalOptimizer extends AbstractModule {

    private final BooleanSetting simplifyInnerCube = registerSetting(new BooleanSetting(
            "simplify_inner_cube", "Simplify Distant Crystals", "Skip outer glass shell pass on End Crystals beyond 20 blocks", true
    ));
    private final IntegerSetting crystalCullDistance = registerSetting(new IntegerSetting(
            "crystal_cull_dist", "Crystal Render Distance", "Max distance to render End Crystals", 48, 16, 96, 4, "m"
    ));
    private final BooleanSetting reduceCrystalExplosionSmoke = registerSetting(new BooleanSetting(
            "reduce_crystal_smoke", "Reduce Explosion Smoke", "Minimize dense white explosion smoke during crystal PvP", true
    ));

    public CrystalOptimizer() {
        super(
                "crystal_optimizer",
                "Crystal Optimizer",
                "Optimizes End Crystal rendering and explosion particle spikes.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean shouldCullCrystal(double squaredDistance) {
        if (!isEnabled()) {
            return false;
        }
        int d = crystalCullDistance.getValue();
        return squaredDistance > (double) (d * d);
    }

    public boolean shouldSimplifyInnerCube(double squaredDistance) {
        return isEnabled() && simplifyInnerCube.isEnabled() && squaredDistance > 400.0;
    }

    public boolean isReduceCrystalExplosionSmoke() {
        return isEnabled() && reduceCrystalExplosionSmoke.isEnabled();
    }
}
