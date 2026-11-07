package com.banglaclient.module.impl.advanced;

import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 290: Performance History
 * Tracks rolling FPS and frame-time history locally for the Dashboard and profiler graphs.
 */
public final class PerformanceHistory extends AbstractModule {

    private final BooleanSetting recordPingSamples = registerSetting(new BooleanSetting(
            "record_ping_samples", "Track Network History", "Include network ping samples in rolling history buffers", true
    ));

    public PerformanceHistory() {
        super(
                "performance_history",
                "Performance History",
                "Tracks rolling FPS and frame-time performance history locally.",
                ModuleCategory.ADVANCED_PERFORMANCE,
                true
        );
    }

    public int getAverageRecentFps() {
        int[] hist = PerformanceTracker.getInstance().getFpsHistory();
        long sum = 0L;
        int count = 0;
        for (int v : hist) {
            if (v > 0) {
                sum += v;
                count++;
            }
        }
        return count > 0 ? (int) (sum / count) : PerformanceTracker.getInstance().getCurrentFps();
    }

    public boolean isRecordPingSamples() {
        return isEnabled() && recordPingSamples.isEnabled();
    }
}
