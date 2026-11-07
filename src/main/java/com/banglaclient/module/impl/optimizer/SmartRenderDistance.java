package com.banglaclient.module.impl.optimizer;

import com.banglaclient.core.ClientCore;
import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.event.ClientTickEvent;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 012: Smart Render Distance
 *Monitors 1% Low FPS stability and reduces view distance when frame-time spikes or combat
 * instability are detected.
 */
public final class SmartRenderDistance extends AbstractModule {

    private final IntegerSetting minOnePercentLow = registerSetting(new IntegerSetting(
            "min_1pct_low", "1% Low Threshold", "Reduce view distance if 1% Low FPS falls below this value", 55, 25, 144, 5, " FPS"
    ));
    private final IntegerSetting combatFallbackChunks = registerSetting(new IntegerSetting(
            "fallback_chunks", "Safe Fallback Chunks", "Target chunk distance during severe frame instability", 6, 2, 16, 1, " chunks"
    ));

    private long lastTriggerMillis = 0L;

    public SmartRenderDistance() {
        super(
                "smart_render_distance",
                "Smart Render Distance",
                "Reduces render distance when 1% Low FPS becomes unstable.",
                ModuleCategory.OPTIMIZER,
                false
        );
    }

    @Override
    public void onEnable() {
        ClientCore.getInstance().getEventBus().subscribe(this, ClientTickEvent.class, this::onTick);
    }

    private void onTick(ClientTickEvent event) {
        if (event.getPhase() != ClientTickEvent.Phase.END || mc.world == null || mc.options == null) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now - lastTriggerMillis < 10_000L) {
            return;
        }
        int onePercentLow = PerformanceTracker.getInstance().getOnePercentLowFps();
        int currentDist = mc.options.getViewDistance().getValue();
        int fallback = combatFallbackChunks.getValue();

        if (onePercentLow > 0 && onePercentLow < minOnePercentLow.getValue() && currentDist > fallback) {
            mc.options.getViewDistance().setValue(Math.max(fallback, currentDist - 2));
            lastTriggerMillis = now;
        }
    }
}
