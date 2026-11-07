package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 046: Consumable Optimizer
 * Optimizes client-side food/potion consumable eating animation jitter and mouth crumb particle bursts.
 */
public final class ConsumableOptimizer extends AbstractModule {

    private final IntegerSetting crumbParticlePercent = registerSetting(new IntegerSetting(
            "crumb_particle_pct", "Eating Crumb Density", "Percentage of item-crumb particles spawned in front of the camera", 30, 0, 100, 10, "%"
    ));
    private final BooleanSetting smoothEatBobbing = registerSetting(new BooleanSetting(
            "smooth_eat_bobbing", "Smooth Eating Animation", "Use frame-interpolated vertical oscillation while eating", true
    ));

    public ConsumableOptimizer() {
        super(
                "consumable_optimizer",
                "Consumable Optimizer",
                "Optimizes client-side food/consumable crumb particles and animation.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public int getCrumbParticlePercent() {
        return isEnabled() ? crumbParticlePercent.getValue() : 100;
    }

    public boolean isSmoothEatBobbing() {
        return isEnabled() && smoothEatBobbing.isEnabled();
    }
}
