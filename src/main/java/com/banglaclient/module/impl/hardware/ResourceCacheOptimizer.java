package com.banglaclient.module.impl.hardware;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.util.ClientResourceManager;

/**
 * Module 080: Resource Cache Optimizer
 * Reduces repeated resource manager existence lookups during GUI and HUD rendering.
 */
public final class ResourceCacheOptimizer extends AbstractModule {

    private final BooleanSetting cacheResourcePresence = registerSetting(new BooleanSetting(
            "cache_resource_presence", "Cache Resource Lookups", "Avoid querying Minecraft ResourceManager every frame for UI textures", true
    ));

    public ResourceCacheOptimizer() {
        super(
                "resource_cache_optimizer",
                "Resource Cache Optimizer",
                "Reduces repeated resource lookup overhead per frame.",
                ModuleCategory.RAM_CPU_GPU,
                true
        );
    }

    @Override
    public void onDisable() {
        ClientResourceManager.invalidateCache();
    }

    public boolean isCacheResourcePresence() {
        return isEnabled() && cacheResourcePresence.isEnabled();
    }
}
