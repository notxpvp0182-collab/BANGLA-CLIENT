package com.banglaclient.module.impl.performance;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 072: Fast Server Switching (Performance & FPS Category)
 * Streamlines client-side server switching and dimension/sub-server transfers by avoiding
 * full sound/resource teardown when server resource packs haven't changed.
 */
public final class FastServerSwitchingPerf extends AbstractModule {

    private final BooleanSetting preserveAudioEngine = registerSetting(new BooleanSetting(
            "preserve_audio_engine", "Preserve Sound Engine", "Keep sound system warm when switching between Bungee/Velocity lobbies", true
    ));
    private final BooleanSetting fastScoreboardReset = registerSetting(new BooleanSetting(
            "fast_scoreboard_reset", "Fast State Reset", "Clear cached entity/tablist maps in bulk on disconnect", true
    ));

    public FastServerSwitchingPerf() {
        super(
                "fast_server_switching_perf",
                "Fast Server Switching",
                "Streamlines client-side server and lobby switching.",
                ModuleCategory.PERFORMANCE_FPS,
                true
        );
    }

    public boolean isPreserveAudioEngine() {
        return isEnabled() && preserveAudioEngine.isEnabled();
    }

    public boolean isFastScoreboardReset() {
        return isEnabled() && fastScoreboardReset.isEnabled();
    }
}
