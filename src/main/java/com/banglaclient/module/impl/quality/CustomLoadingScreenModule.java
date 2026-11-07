package com.banglaclient.module.impl.quality;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 295: Custom Loading Screen
 * Provides the dark/crimson animated BANGLACLIENT Loading Screen with smooth 100% completion transition.
 */
public final class CustomLoadingScreenModule extends AbstractModule {

    private final BooleanSetting smoothCompletionFade = registerSetting(new BooleanSetting(
            "smooth_completion_fade", "Smooth 100% Transition", "Use fade/scale transition at 100% instead of abrupt snap", true
    ));

    public CustomLoadingScreenModule() {
        super(
                "custom_loading_screen",
                "Custom Loading Screen",
                "Provides the animated BANGLACLIENT loading screen.",
                ModuleCategory.CLIENT_QUALITY_FEATURES,
                true
        );
    }

    public boolean isSmoothCompletionFade() {
        return isEnabled() && smoothCompletionFade.isEnabled();
    }
}
