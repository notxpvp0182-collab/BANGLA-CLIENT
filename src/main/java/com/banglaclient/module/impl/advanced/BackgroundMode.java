package com.banglaclient.module.impl.advanced;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 286: Background Mode
 * Aggressively reduces resource usage when the game window is unfocused or running in the background.
 */
public final class BackgroundMode extends AbstractModule {

    private final IntegerSetting backgroundFps = registerSetting(new IntegerSetting(
            "background_mode_fps", "Background FPS", "Frame rate while window is unfocused", 10, 5, 30, 5, " FPS"
    ));

    public BackgroundMode() {
        super(
                "background_mode",
                "Background Mode",
                "Reduces resource usage when the game is unfocused/backgrounded.",
                ModuleCategory.ADVANCED_PERFORMANCE,
                true
        );
    }

    public int getBackgroundFps() {
        return backgroundFps.getValue();
    }
}
