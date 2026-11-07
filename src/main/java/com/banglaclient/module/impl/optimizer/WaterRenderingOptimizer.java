package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 024: Water Rendering Optimizer
 * Optimizes water surface/side face rendering and suppresses ambient underwater bubble/suspended particles.
 */
public final class WaterRenderingOptimizer extends AbstractModule {

    private final BooleanSetting cullSubmergedSides = registerSetting(new BooleanSetting(
            "cull_submerged_sides", "Cull Submerged Side Faces", "Hide internal water faces against solid opaque blocks", true
    ));
    private final BooleanSetting suppressAmbientWaterParticles = registerSetting(new BooleanSetting(
            "suppress_water_particles", "Reduce Suspended Particles", "Skip ambient underwater suspended dust particles", true
    ));

    public WaterRenderingOptimizer() {
        super(
                "water_rendering_optimizer",
                "Water Rendering Optimizer",
                "Optimizes water face culling and ambient underwater particles.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean isCullSubmergedSides() {
        return isEnabled() && cullSubmergedSides.isEnabled();
    }

    public boolean isSuppressAmbientWaterParticles() {
        return isEnabled() && suppressAmbientWaterParticles.isEnabled();
    }
}
