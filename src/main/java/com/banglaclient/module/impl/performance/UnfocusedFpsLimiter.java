package com.banglaclient.module.impl.performance;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 064: Unfocused FPS Limiter
 * Reduces FPS and GPU power draw when the Minecraft window loses OS focus (Alt-Tab).
 */
public final class UnfocusedFpsLimiter extends AbstractModule {

    private final IntegerSetting unfocusedMaxFps = registerSetting(new IntegerSetting(
            "unfocused_max_fps", "Unfocused FPS Limit", "Target frame rate when Minecraft is minimized or unfocused", 15, 5, 60, 5, " FPS"
    ));
    private final BooleanSetting muteSoundWhenUnfocused = registerSetting(new BooleanSetting(
            "mute_when_unfocused", "Reduce Background Audio", "Lower ambient sound processing while unfocused", false
    ));

    public UnfocusedFpsLimiter() {
        super(
                "unfocused_fps_limiter",
                "Unfocused FPS Limiter",
                "Reduces FPS when Minecraft loses window focus.",
                ModuleCategory.PERFORMANCE_FPS,
                true
        );
    }

    public int getUnfocusedFpsCap() {
        return unfocusedMaxFps.getValue();
    }

    public boolean isMuteSoundWhenUnfocused() {
        return isEnabled() && muteSoundWhenUnfocused.isEnabled();
    }
}
