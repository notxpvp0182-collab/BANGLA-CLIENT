package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 028: XP Optimizer
 * Optimizes rendering and color oscillation math of large clusters of ExperienceOrbEntity instances.
 */
public final class XpOptimizer extends AbstractModule {

    private final IntegerSetting maxOrbRenderDistance = registerSetting(new IntegerSetting(
            "max_orb_dist", "Orb Render Distance", "Cull XP orbs beyond this distance", 32, 8, 64, 4, "m"
    ));
    private final BooleanSetting staticOrbColor = registerSetting(new BooleanSetting(
            "static_orb_color", "Static Orb Color", "Skip per-frame sine/cosine RGB color shifting on XP orbs", true
    ));

    public XpOptimizer() {
        super(
                "xp_optimizer",
                "XP Optimizer",
                "Optimizes rendering of large numbers of XP orbs at farms.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean shouldCullXpOrb(double squaredDistance) {
        if (!isEnabled()) {
            return false;
        }
        int d = maxOrbRenderDistance.getValue();
        return squaredDistance > (double) (d * d);
    }

    public boolean isStaticOrbColor() {
        return isEnabled() && staticOrbColor.isEnabled();
    }
}
