package com.banglaclient.module.impl.hardware;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 079: Model Cache Optimizer
 * Caches reusable item/block baked quad lists for static inventory items and HUD icons.
 */
public final class ModelCacheOptimizer extends AbstractModule {

    private final BooleanSetting cacheStaticItemQuads = registerSetting(new BooleanSetting(
            "cache_static_item_quads", "Cache HUD Item Quads", "Reuse baked model lookups for Armor HUD and Inventory HUD items", true
    ));

    public ModelCacheOptimizer() {
        super(
                "model_cache_optimizer",
                "Model Cache Optimizer",
                "Caches reusable model data for HUD and inventory rendering.",
                ModuleCategory.RAM_CPU_GPU,
                true
        );
    }

    public boolean isCacheStaticItemQuads() {
        return isEnabled() && cacheStaticItemQuads.isEnabled();
    }
}
