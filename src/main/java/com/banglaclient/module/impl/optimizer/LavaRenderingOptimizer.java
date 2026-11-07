package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 025: Lava Rendering Optimizer
 * Optimizes lava rendering in the Nether and lava lakes by throttling popping lava embers and smoke.
 */
public final class LavaRenderingOptimizer extends AbstractModule {

    private final BooleanSetting throttleLavaPopParticles = registerSetting(new BooleanSetting(
            "throttle_lava_pops", "Throttle Lava Embers", "Reduce popping lava spark particles in large lava oceans", true
    ));
    private final BooleanSetting simplifyDistantLavaFlow = registerSetting(new BooleanSetting(
            "simplify_distant_lava", "Simplify Distant Flow", "Reduce flow vertex interpolation beyond 48 blocks", true
    ));

    public LavaRenderingOptimizer() {
        super(
                "lava_rendering_optimizer",
                "Lava Rendering Optimizer",
                "Optimizes lava rendering and throttles Nether lava ember lag.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean isThrottleLavaPopParticles() {
        return isEnabled() && throttleLavaPopParticles.isEnabled();
    }

    public boolean isSimplifyDistantLavaFlow() {
        return isEnabled() && simplifyDistantLavaFlow.isEnabled();
    }
}
