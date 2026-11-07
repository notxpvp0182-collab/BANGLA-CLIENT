package com.banglaclient.module.impl.performance;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 069: Loading Screen Optimizer
 * Reduces unnecessary background world/panorama rendering work during loading overlays so CPU/IO
 * threads finish resource reloads faster.
 */
public final class LoadingScreenOptimizer extends AbstractModule {

    private final BooleanSetting skipBackgroundPanorama = registerSetting(new BooleanSetting(
            "skip_bg_panorama", "Skip Panorama Under Overlay", "Avoid rendering the 3D panorama cube while the loading overlay is opaque", true
    ));

    public LoadingScreenOptimizer() {
        super(
                "loading_screen_optimizer",
                "Loading Screen Optimizer",
                "Reduces unnecessary rendering work during loading screens.",
                ModuleCategory.PERFORMANCE_FPS,
                true
        );
    }

    public boolean isSkipBackgroundPanorama() {
        return isEnabled() && skipBackgroundPanorama.isEnabled();
    }
}
