package com.banglaclient.module.impl.quality;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 300: Startup Cache
 * Caches frequently used startup metadata, external mod discovery, and hardware telemetry strings.
 */
public final class StartupCache extends AbstractModule {

    private final BooleanSetting cacheExternalModScan = registerSetting(new BooleanSetting(
            "cache_external_mod_scan", "Cache Mod Discovery", "Scan installed external mod entrypoints once at startup", true
    ));

    public StartupCache() {
        super(
                "startup_cache",
                "Startup Cache",
                "Caches frequently used startup metadata and mod discovery.",
                ModuleCategory.CLIENT_QUALITY_FEATURES,
                true
        );
    }

    public boolean isCacheExternalModScan() {
        return isEnabled() && cacheExternalModScan.isEnabled();
    }
}
