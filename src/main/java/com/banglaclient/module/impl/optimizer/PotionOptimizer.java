package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 032: Potion Optimizer
 * Optimizes potion-related client effects, splash glass/swirl particle bursts, and first-person screen clutter.
 */
public final class PotionOptimizer extends AbstractModule {

    private final BooleanSetting hideOwnSwirlParticles = registerSetting(new BooleanSetting(
            "hide_own_swirls", "Hide First-Person Swirls", "Suppress status effect swirl particles blocking your own camera", true
    ));
    private final IntegerSetting splashParticleMultiplier = registerSetting(new IntegerSetting(
            "splash_particle_pct", "Splash Particle Density", "Percentage of splash potion break particles to spawn", 40, 10, 100, 10, "%"
    ));

    public PotionOptimizer() {
        super(
                "potion_optimizer",
                "Potion Optimizer",
                "Optimizes potion-related client effects and splash particles.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean shouldHideOwnSwirls() {
        return isEnabled() && hideOwnSwirlParticles.isEnabled();
    }

    public int getSplashParticlePercentage() {
        return isEnabled() ? splashParticleMultiplier.getValue() : 100;
    }
}
