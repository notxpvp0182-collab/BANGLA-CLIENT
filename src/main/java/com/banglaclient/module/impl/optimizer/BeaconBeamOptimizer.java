package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 027: Beacon Beam Optimizer
 * Reduces unnecessary beacon beam outer translucent shell rendering and culls distant beacon beams.
 */
public final class BeaconBeamOptimizer extends AbstractModule {

    private final IntegerSetting maxBeamDistance = registerSetting(new IntegerSetting(
            "max_beam_dist", "Max Beam Distance", "Cull beacon beams further than this distance", 64, 16, 160, 8, "m"
    ));
    private final BooleanSetting skipOuterTranslucentGlow = registerSetting(new BooleanSetting(
            "skip_outer_glow", "Fast Single-Pass Beam", "Skip outer translucent glow cylinder on distant beacons", true
    ));

    public BeaconBeamOptimizer() {
        super(
                "beacon_beam_optimizer",
                "Beacon Beam Optimizer",
                "Reduces unnecessary beacon beam translucent rendering work.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public boolean shouldCullBeam(double squaredDistance) {
        if (!isEnabled()) {
            return false;
        }
        int d = maxBeamDistance.getValue();
        return squaredDistance > (double) (d * d);
    }

    public boolean isSkipOuterTranslucentGlow() {
        return isEnabled() && skipOuterTranslucentGlow.isEnabled();
    }
}
