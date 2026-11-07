package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 040: Pearl Optimizer
 * Optimizes Ender Pearl flight visual visibility and landing portal particle bursts.
 */
public final class PearlOptimizer extends AbstractModule {

    private final BooleanSetting reduceLandingPortalBurst = registerSetting(new BooleanSetting(
            "reduce_portal_burst", "Reduce Landing Particles", "Reduce dense purple portal particles when an Ender Pearl lands", true
    ));
    private final BooleanSetting crispPearlBillboard = registerSetting(new BooleanSetting(
            "crisp_pearl_billboard", "Crisp Pearl Tracking", "Ensure smooth frame-interpolated pearl position rendering", true
    ));

    public PearlOptimizer() {
        super(
                "pearl_optimizer",
                "Pearl Optimizer",
                "Optimizes Ender Pearl trajectory rendering and landing effects.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean isReduceLandingPortalBurst() {
        return isEnabled() && reduceLandingPortalBurst.isEnabled();
    }

    public boolean isCrispPearlBillboard() {
        return isEnabled() && crispPearlBillboard.isEnabled();
    }
}
