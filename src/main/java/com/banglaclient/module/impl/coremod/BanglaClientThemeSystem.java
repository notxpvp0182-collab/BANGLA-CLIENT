package com.banglaclient.module.impl.coremod;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ModeSetting;
import com.banglaclient.theme.ThemeManager;

/**
 * Module 305: BANGLACLIENT Theme System
 * Provides the central client theme engine (Default Red, Dark Red, Black & White + Red, Custom Theme).
 */
public final class BanglaClientThemeSystem extends AbstractModule {

    private final ModeSetting activeThemeMode = registerSetting(new ModeSetting(
            "active_theme_mode", "Active Theme", "Select the active BANGLACLIENT visual theme",
            "default_red", "default_red", "dark_red", "bw_red", "custom"
    ));

    public BanglaClientThemeSystem() {
        super(
                "banglaclient_theme_system",
                "BANGLACLIENT Theme System",
                "Provides the Black + White + Red client theme engine.",
                ModuleCategory.BANGLACLIENT_CORE,
                true
        );
        activeThemeMode.onChange((oldV, newV) -> ThemeManager.getInstance().setActiveTheme(newV));
    }

    @Override
    public void onEnable() {
        ThemeManager.getInstance().setActiveTheme(activeThemeMode.getValue());
    }
}
