package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 044: String Optimizer
 * Optimizes tripwire/string cutout rendering at long distances while keeping nearby tripwires visible.
 */
public final class StringOptimizer extends AbstractModule {

    private final IntegerSetting distantCullRadius = registerSetting(new IntegerSetting(
            "string_cull_radius", "Distant String Cull", "Skip tripwire quad sub-passes beyond this distance", 32, 16, 64, 4, "m"
    ));
    private final BooleanSetting preserveNearbyVisibility = registerSetting(new BooleanSetting(
            "preserve_nearby", "High-Contrast Nearby", "Ensure tripwires within 16 blocks are never culled", true
    ));

    public StringOptimizer() {
        super(
                "string_optimizer",
                "String Optimizer",
                "Optimizes distant tripwire/string cutout rendering workload.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean shouldCullDistantString(double squaredDistance) {
        if (!isEnabled()) {
            return false;
        }
        if (preserveNearbyVisibility.isEnabled() && squaredDistance <= 256.0) {
            return false;
        }
        int d = distantCullRadius.getValue();
        return squaredDistance > (double) (d * d);
    }
}
