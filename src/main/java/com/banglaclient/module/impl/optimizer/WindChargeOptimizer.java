package com.banglaclient.module.impl.optimizer;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 043: Wind Charge Optimizer
 * Optimizes Minecraft 1.21+ Wind Charge projectile model rotation and gust burst particles.
 */
public final class WindChargeOptimizer extends AbstractModule {

    private final IntegerSetting gustParticlePercent = registerSetting(new IntegerSetting(
            "gust_particle_pct", "Gust Particle Density", "Percentage of wind burst particles to render on impact", 45, 10, 100, 5, "%"
    ));
    private final BooleanSetting cullDistantWindCharges = registerSetting(new BooleanSetting(
            "cull_distant_wind_charges", "Cull Distant Charges", "Skip rendering flying Wind Charges beyond 48 blocks", true
    ));

    public WindChargeOptimizer() {
        super(
                "wind_charge_optimizer",
                "Wind Charge Optimizer",
                "Optimizes Wind Charge projectile rendering and gust burst particles.",
                ModuleCategory.OPTIMIZER,
                true
        );
    }

    public int getGustParticlePercent() {
        return isEnabled() ? gustParticlePercent.getValue() : 100;
    }

    public boolean shouldCullWindCharge(double squaredDistance) {
        return isEnabled() && cullDistantWindCharges.isEnabled() && squaredDistance > 2304.0;
    }
}
