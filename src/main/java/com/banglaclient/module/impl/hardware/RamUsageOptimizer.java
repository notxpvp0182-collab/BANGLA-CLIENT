package com.banglaclient.module.impl.hardware;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.util.ClientResourceManager;

/**
 * Module 074: RAM Usage Optimizer
 * Reduces avoidable client memory footprint by trimming transient caches on world change.
 */
public final class RamUsageOptimizer extends AbstractModule {

    private final BooleanSetting trimCachesOnDisconnect = registerSetting(new BooleanSetting(
            "trim_caches_disconnect", "Trim Caches On Disconnect", "Release cached world/UI lookup tables when leaving a server", true
    ));
    private final BooleanSetting reuseEventInstances = registerSetting(new BooleanSetting(
            "reuse_event_instances", "Zero-Allocation Events", "Reuse singleton tick and render event wrappers", true
    ));

    public RamUsageOptimizer() {
        super(
                "ram_usage_optimizer",
                "RAM Usage Optimizer",
                "Reduces avoidable client memory allocations and trims stale caches.",
                ModuleCategory.RAM_CPU_GPU,
                true
        );
    }

    public void onWorldUnloaded() {
        if (isEnabled() && trimCachesOnDisconnect.isEnabled()) {
            ClientResourceManager.invalidateCache();
        }
    }

    public boolean isReuseEventInstances() {
        return isEnabled() && reuseEventInstances.isEnabled();
    }
}
