package com.banglaclient.module.impl.performance;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 065: Menu FPS Limiter
 * Provides a dedicated FPS limit while on the Main Menu or out-of-world screens to prevent unnecessary GPU coil whine.
 */
public final class MenuFpsLimiter extends AbstractModule {

    private final IntegerSetting menuFpsCap = registerSetting(new IntegerSetting(
            "menu_fps_cap", "Menu FPS Limit", "Maximum frame rate in main menus", 60, 30, 144, 10, " FPS"
    ));

    public MenuFpsLimiter() {
        super(
                "menu_fps_limiter",
                "Menu FPS Limiter",
                "Provides a separate FPS limit for menus.",
                ModuleCategory.PERFORMANCE_FPS,
                true
        );
    }

    public int getMenuFpsCap() {
        return menuFpsCap.getValue();
    }
}
