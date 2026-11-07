package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 034: Mace Optimizer
 * Optimizes Minecraft 1.21+ Mace smash ground-pillar dust bursts and shockwave particle spikes during PvP.
 */
public final class MaceOptimizer extends AbstractModule {

    private final BooleanSetting limitSmashDustParticles = registerSetting(new BooleanSetting(
            "limit_smash_dust", "Limit Smash Dust Burst", "Cap heavy block-dust particle bursts during Mace smash attacks", true
    ));
    private final BooleanSetting smoothFallCameraTransition = registerSetting(new BooleanSetting(
            "smooth_fall_camera", "Smooth Smash Camera", "Prevent frame jitter on high-velocity Mace impact", true
    ));

    public MaceOptimizer() {
        super(
                "mace_optimizer",
                "Mace Optimizer",
                "Optimizes client-side Mace smash effects and shockwave particles.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean isLimitSmashDustParticles() {
        return isEnabled() && limitSmashDustParticles.isEnabled();
    }

    public boolean isSmoothFallCameraTransition() {
        return isEnabled() && smoothFallCameraTransition.isEnabled();
    }
}
