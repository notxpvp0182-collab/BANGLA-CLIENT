package com.banglaclient.module.impl.quality;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.theme.ThemeManager;

/**
 * Module 296: Dark Mode GUI
 * Ensures the overall dark carbon/black + crimson red BANGLACLIENT interface aesthetic is active.
 */
public final class DarkModeGui extends AbstractModule {

    public DarkModeGui() {
        super(
                "dark_mode_gui",
                "Dark Mode GUI",
                "Provides the signature dark carbon and red interface.",
                ModuleCategory.CLIENT_QUALITY_FEATURES,
                true
        );
    }

    @Override
    public void onEnable() {
        ThemeManager.getInstance().setActiveTheme("default_red");
    }
}
