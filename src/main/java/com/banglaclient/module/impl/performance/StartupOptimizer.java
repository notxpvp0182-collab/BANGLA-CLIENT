package com.banglaclient.module.impl.performance;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 070: Startup Optimizer
 * Reduces unnecessary client startup work and defers non-critical telemetry caches until the main menu opens.
 */
public final class StartupOptimizer extends AbstractModule {

    private final BooleanSetting deferNonCriticalCaches = registerSetting(new BooleanSetting(
            "defer_non_critical_caches", "Lazy Telemetry Init", "Defer non-essential hardware & external mod scans until first screen render", true
    ));

    public StartupOptimizer() {
        super(
                "startup_optimizer",
                "Startup Optimizer",
                "Reduces unnecessary client startup work.",
                ModuleCategory.PERFORMANCE_FPS,
                true
        );
    }

    public boolean isDeferNonCriticalCaches() {
        return isEnabled() && deferNonCriticalCaches.isEnabled();
    }
}
