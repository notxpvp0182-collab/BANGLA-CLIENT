package com.banglaclient.module.impl.performance;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 066: World FPS Limiter
 * Provides a dedicated gameplay FPS limit when loaded inside a world.
 */
public final class WorldFpsLimiter extends AbstractModule {

    private final IntegerSetting gameplayFpsCap = registerSetting(new IntegerSetting(
            "gameplay_fps_cap", "World FPS Limit", "Maximum frame rate during active gameplay (260 = Unlimited)", 260, 30, 260, 10, " FPS"
    ));

    public WorldFpsLimiter() {
        super(
                "world_fps_limiter",
                "World FPS Limiter",
                "Provides a separate FPS limit for in-world gameplay.",
                ModuleCategory.PERFORMANCE_FPS,
                false
        );
    }

    public int getGameplayFpsCap() {
        return gameplayFpsCap.getValue();
    }
}
