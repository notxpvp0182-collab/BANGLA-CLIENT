package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 029: TNT Optimizer
 * Optimizes client-side primed TNT rendering, fuse smoke, and mass-explosion frame drops
 * without changing server-authoritative explosion mechanics.
 */
public final class TntOptimizer extends AbstractModule {

    private final BooleanSetting disableFuseSmoke = registerSetting(new BooleanSetting(
            "disable_fuse_smoke", "Disable TNT Fuse Smoke", "Suppress smoke particles spawned every tick by primed TNT", true
    ));
    private final IntegerSetting maxTntRenderDistance = registerSetting(new IntegerSetting(
            "max_tnt_dist", "Max TNT Render Distance", "Cull primed TNT entities beyond this distance", 64, 24, 128, 8, "m"
    ));
    private final BooleanSetting simplifyFlashOverlay = registerSetting(new BooleanSetting(
            "simplify_flash", "Fast Swell Render", "Skip block-scale matrix expansion when >20 TNT are nearby", true
    ));

    public TntOptimizer() {
        super(
                "tnt_optimizer",
                "TNT Optimizer",
                "Optimizes client-side TNT rendering and mass cannoning FPS.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean shouldCullTnt(double squaredDistance) {
        if (!isEnabled()) {
            return false;
        }
        int d = maxTntRenderDistance.getValue();
        return squaredDistance > (double) (d * d);
    }

    public boolean isDisableFuseSmoke() {
        return isEnabled() && disableFuseSmoke.isEnabled();
    }

    public boolean isSimplifyFlashOverlay() {
        return isEnabled() && simplifyFlashOverlay.isEnabled();
    }
}
