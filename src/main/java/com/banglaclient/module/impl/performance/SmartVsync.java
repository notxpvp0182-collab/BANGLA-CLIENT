package com.banglaclient.module.impl.performance;

import com.banglaclient.core.ClientCore;
import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.event.ClientTickEvent;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 067: Smart VSync
 * Automatically disables VSync when frame rate drops below the monitor refresh threshold to prevent
 * half-refresh stuttering, and re-enables it when frame rate is well above the threshold.
 */
public final class SmartVsync extends AbstractModule {

    private final IntegerSetting refreshThresholdFps = registerSetting(new IntegerSetting(
            "refresh_threshold_fps", "Refresh Threshold", "Disable VSync if FPS drops below this rate", 58, 30, 165, 2, " FPS"
    ));

    public SmartVsync() {
        super(
                "smart_vsync",
                "Smart VSync",
                "Dynamically toggles VSync to prevent below-refresh input lag.",
                ModuleCategory.PERFORMANCE_FPS,
                false
        );
    }

    @Override
    public void onEnable() {
        ClientCore.getInstance().getEventBus().subscribe(this, ClientTickEvent.class, this::onTick);
    }

    private void onTick(ClientTickEvent event) {
        if (event.getPhase() != ClientTickEvent.Phase.END || mc.options == null) {
            return;
        }
        int fps = PerformanceTracker.getInstance().getCurrentFps();
        int threshold = refreshThresholdFps.getValue();
        boolean currentVsync = mc.options.getEnableVsync().getValue();

        if (fps > 0 && fps < threshold && currentVsync) {
            mc.options.getEnableVsync().setValue(false);
        } else if (fps > threshold + 15 && !currentVsync) {
            mc.options.getEnableVsync().setValue(true);
        }
    }
}
