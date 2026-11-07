package com.banglaclient.module.impl.client;

import com.banglaclient.config.ConfigManager;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.Module;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.setting.ActionSetting;

/**
 * Module 269: One-Click Optimization
 * Applies recommended safe rendering, culling, and memory optimizations in a single click.
 */
public final class OneClickOptimization extends AbstractModule {

    public OneClickOptimization() {
        super(
                "one_click_optimization",
                "One-Click Optimization",
                "Applies recommended safe performance settings in one click.",
                ModuleCategory.CLIENT_SETTINGS,
                false
        );

        registerSetting(new ActionSetting(
                "run_one_click_opt",
                "Optimize Client Now",
                "Enable all safe rendering, culling, and memory optimizers",
                "Optimize Now",
                this::applyOneClickOptimizations
        ));
    }

    @Override
    public void onEnable() {
        applyOneClickOptimizations();
    }

    public void applyOneClickOptimizations() {
        for (Module m : ModuleRegistry.getInstance().getAllModules()) {
            if (m.getCategory() == ModuleCategory.OPTIMIZER || m.getCategory() == ModuleCategory.RAM_CPU_GPU) {
                if (!m.getId().equals("dynamic_render_distance") && !m.getId().equals("smart_render_distance")) {
                    m.setEnabled(true);
                }
            }
        }
        ConfigManager.getInstance().saveConfig();
    }
}
