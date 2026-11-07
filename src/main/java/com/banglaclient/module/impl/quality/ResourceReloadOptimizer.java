package com.banglaclient.module.impl.quality;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.util.ClientResourceManager;

/**
 * Module 299: Resource Reload Optimizer
 * Reduces unnecessary resource reload work and refreshes BANGLACLIENT texture caches cleanly on F3+T.
 */
public final class ResourceReloadOptimizer extends AbstractModule {

    private final BooleanSetting skipUnchangedSounds = registerSetting(new BooleanSetting(
            "skip_unchanged_sounds", "Fast Sound Table Check", "Avoid redundant sound engine re-initialization when packs haven't changed", true
    ));

    public ResourceReloadOptimizer() {
        super(
                "resource_reload_optimizer",
                "Resource Reload Optimizer",
                "Reduces unnecessary work during F3+T resource reloads.",
                ModuleCategory.CLIENT_QUALITY_FEATURES,
                true
        );
    }

    public void onResourceReloadFinished() {
        ClientResourceManager.invalidateCache();
    }

    public boolean isSkipUnchangedSounds() {
        return isEnabled() && skipUnchangedSounds.isEnabled();
    }
}
