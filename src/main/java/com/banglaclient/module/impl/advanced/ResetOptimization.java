package com.banglaclient.module.impl.advanced;

import com.banglaclient.config.ConfigManager;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.Module;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.setting.ActionSetting;

/**
 * Module 292: Reset Optimization
 * Restores all optimization settings and categories back to their factory defaults.
 */
public final class ResetOptimization extends AbstractModule {

    public ResetOptimization() {
        super(
                "reset_optimization",
                "Reset Optimization",
                "Restores all optimization settings to factory defaults.",
                ModuleCategory.ADVANCED_PERFORMANCE,
                false
        );

        registerSetting(new ActionSetting(
                "reset_all_opts_btn",
                "Restore Default Optimizations",
                "Reset all Optimizer, Performance, and Hardware modules to defaults",
                "Reset To Defaults",
                this::resetAllOptimizations
        ));
    }

    @Override
    public void onEnable() {
        resetAllOptimizations();
        setEnabled(false);
    }

    public void resetAllOptimizations() {
        for (Module m : ModuleRegistry.getInstance().getAllModules()) {
            if (m.getCategory() == ModuleCategory.OPTIMIZER
                    || m.getCategory() == ModuleCategory.PERFORMANCE_FPS
                    || m.getCategory() == ModuleCategory.RAM_CPU_GPU
                    || m.getCategory() == ModuleCategory.ADVANCED_PERFORMANCE) {
                m.resetToDefaults();
            }
        }
        OptimizationLog.recordEntry("Restored all optimization modules to factory defaults.");
        ConfigManager.getInstance().saveConfig();
    }
}
