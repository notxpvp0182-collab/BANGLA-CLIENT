package com.banglaclient.module.impl.advanced;

import com.banglaclient.config.ConfigManager;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.Module;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.module.ModuleRegistry;

/**
 * Module 293: Master Performance Switch
 * Enables or disables all core BANGLACLIENT performance optimizations together.
 */
public final class MasterPerformanceSwitch extends AbstractModule {

    public MasterPerformanceSwitch() {
        super(
                "master_performance_switch",
                "Master Performance Switch",
                "Enables or disables major performance optimizations together.",
                ModuleCategory.ADVANCED_PERFORMANCE,
                true
        );
    }

    @Override
    public void onEnable() {
        setCoreOptimizersState(true);
    }

    @Override
    public void onDisable() {
        setCoreOptimizersState(false);
    }

    private void setCoreOptimizersState(boolean state) {
        for (Module m : ModuleRegistry.getInstance().getByCategory(ModuleCategory.OPTIMIZER)) {
            if (!m.getId().equals("dynamic_render_distance") && !m.getId().equals("smart_render_distance")) {
                m.setEnabled(state);
            }
        }
        ConfigManager.getInstance().saveConfig();
    }
}
