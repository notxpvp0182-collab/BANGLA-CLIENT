package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 041: Shield Optimizer
 * Optimizes shield raise/lower animation interpolation and distant banner pattern rendering on shields.
 */
public final class ShieldOptimizer extends AbstractModule {

    private final BooleanSetting smoothRaiseTransition = registerSetting(new BooleanSetting(
            "smooth_raise_transition", "Smooth Block Transition", "Eliminate first-person frame hitching when raising a shield", true
    ));
    private final BooleanSetting skipDistantShieldPatterns = registerSetting(new BooleanSetting(
            "skip_distant_patterns", "Cull Distant Shield Banners", "Skip complex multi-layer banner textures on shields beyond 20 blocks", true
    ));

    public ShieldOptimizer() {
        super(
                "shield_optimizer",
                "Shield Optimizer",
                "Optimizes shield rendering, blocking transitions, and banner layers.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean isSmoothRaiseTransition() {
        return isEnabled() && smoothRaiseTransition.isEnabled();
    }

    public boolean shouldSkipBannerLayers(double squaredDistance) {
        return isEnabled() && skipDistantShieldPatterns.isEnabled() && squaredDistance > 400.0;
    }
}
