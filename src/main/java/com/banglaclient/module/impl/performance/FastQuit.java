package com.banglaclient.module.impl.performance;

import com.banglaclient.config.ConfigManager;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 073: Fast Quit
 * Reduces unnecessary shutdown delay while ensuring BANGLACLIENT configurations are flushed atomically.
 */
public final class FastQuit extends AbstractModule {

    private final BooleanSetting asyncTelemetryFlush = registerSetting(new BooleanSetting(
            "async_telemetry_flush", "Skip Non-Critical Flush", "Avoid waiting on non-essential telemetry threads during game exit", true
    ));

    public FastQuit() {
        super(
                "fast_quit",
                "Fast Quit",
                "Reduces unnecessary shutdown delay while saving config safely.",
                ModuleCategory.PERFORMANCE_FPS,
                true
        );
    }

    public void onPreQuit() {
        if (isEnabled()) {
            ConfigManager.getInstance().saveConfig();
        }
    }

    public boolean isAsyncTelemetryFlush() {
        return isEnabled() && asyncTelemetryFlush.isEnabled();
    }
}
