package com.banglaclient.module.impl.optimizer;

import com.banglaclient.core.ClientCore;
import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.event.ClientTickEvent;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 011: Dynamic Render Distance
 * Automatically scales client view distance between configured minimum and maximum bounds
 * based on rolling FPS performance with a cooldown to prevent rapid chunk rebuild thrashing.
 */
public final class DynamicRenderDistance extends AbstractModule {

    private final IntegerSetting minChunks = registerSetting(new IntegerSetting(
            "min_chunks", "Minimum Distance", "Lowest render distance when FPS drops", 6, 2, 16, 1, " chunks"
    ));
    private final IntegerSetting maxChunks = registerSetting(new IntegerSetting(
            "max_chunks", "Maximum Distance", "Highest render distance when FPS is stable", 12, 4, 32, 1, " chunks"
    ));
    private final IntegerSetting targetFps = registerSetting(new IntegerSetting(
            "target_fps", "Target FPS", "Desired minimum FPS threshold", 90, 30, 240, 5, " FPS"
    ));

    private long lastAdjustmentTimeMillis = 0L;

    public DynamicRenderDistance() {
        super(
                "dynamic_render_distance",
                "Dynamic Render Distance",
                "Automatically adjusts render distance based on current FPS.",
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
        if (now - lastAdjustmentTimeMillis < 8000L) {
            return;
        }

        int currentFps = PerformanceTracker.getInstance().getCurrentFps();
        int currentDist = mc.options.getViewDistance().getValue();
        int min = Math.min(minChunks.getValue(), maxChunks.getValue());
        int max = Math.max(minChunks.getValue(), maxChunks.getValue());
        int goalFps = targetFps.getValue();

        if (currentFps < goalFps - 15 && currentDist > min) {
            mc.options.getViewDistance().setValue(Math.max(min, currentDist - 1));
            lastAdjustmentTimeMillis = now;
        } else if (currentFps > goalFps + 25 && currentDist < max) {
            mc.options.getViewDistance().setValue(Math.min(max, currentDist + 1));
            lastAdjustmentTimeMillis = now;
        }
    }
}
