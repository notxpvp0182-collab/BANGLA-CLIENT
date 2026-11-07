package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 036: Crossbow Optimizer
 * Optimizes crossbow charging pull-stage model swaps and firework projectile visual clutter.
 */
public final class CrossbowOptimizer extends AbstractModule {

    private final BooleanSetting stabilizePullJitter = registerSetting(new BooleanSetting(
            "stabilize_pull_jitter", "Stabilize Charge Shake", "Remove first-person held-item shake while holding a charged crossbow", true
    ));
    private final BooleanSetting cacheChargedModel = registerSetting(new BooleanSetting(
            "cache_charged_model", "Cache Charged Predicate", "Avoid repeated projectile component lookups per render pass", true
    ));

    public CrossbowOptimizer() {
        super(
                "crossbow_optimizer",
                "Crossbow Optimizer",
                "Optimizes crossbow charging animations and shake visuals.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean isStabilizePullJitter() {
        return isEnabled() && stabilizePullJitter.isEnabled();
    }

    public boolean isCacheChargedModel() {
        return isEnabled() && cacheChargedModel.isEnabled();
    }
}
