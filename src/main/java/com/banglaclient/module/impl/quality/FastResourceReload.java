package com.banglaclient.module.impl.quality;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ActionSetting;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.util.ClientResourceManager;

/**
 * Module 301: Fast Resource Reload
 * Optimizes resource reload handling and provides a one-click texture/resource refresh.
 */
public final class FastResourceReload extends AbstractModule {

    private final BooleanSetting parallelAtlasStitch = registerSetting(new BooleanSetting(
            "parallel_atlas_stitch", "Parallelize Atlas Prep", "Use background worker pool during texture atlas stitching", true
    ));

    public FastResourceReload() {
        super(
                "fast_resource_reload",
                "Fast Resource Reload",
                "Optimizes resource reload speed and atlas preparation.",
                ModuleCategory.CLIENT_QUALITY_FEATURES,
                true
        );

        registerSetting(new ActionSetting(
                "trigger_fast_reload",
                "Reload Resources Now",
                "Clear UI texture cache and reload Minecraft resource packs",
                "Reload Packs",
                () -> {
                    ClientResourceManager.invalidateCache();
                    if (mc != null) {
                        mc.reloadResources();
                    }
                }
        ));
    }

    public boolean isParallelAtlasStitch() {
        return isEnabled() && parallelAtlasStitch.isEnabled();
    }
}
