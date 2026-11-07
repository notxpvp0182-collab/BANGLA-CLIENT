package com.banglaclient.module.impl.performance;

import com.banglaclient.core.ClientCore;
import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.event.ClientTickEvent;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 068: Dynamic Simulation Distance
 * Adjusts client/integrated-server simulation distance according to real-time performance.
 */
public final class DynamicSimulationDistance extends AbstractModule {

    private final IntegerSetting minSimDistance = registerSetting(new IntegerSetting(
            "min_sim_dist", "Min Simulation Chunks", "Minimum simulation distance under heavy load", 5, 5, 12, 1, " chunks"
    ));
    private final IntegerSetting maxSimDistance = registerSetting(new IntegerSetting(
            "max_sim_dist", "Max Simulation Chunks", "Maximum simulation distance when performance is high", 10, 5, 16, 1, " chunks"
    ));

    private long lastUpdateMillis = 0L;

    public DynamicSimulationDistance() {
        super(
                "dynamic_simulation_distance",
                "Dynamic Simulation Distance",
                "Adjusts simulation distance according to performance.",
                ModuleCategory.PERFORMANCE_FPS,
                false
        );
    }

    @Override
    public void onEnable() {
        ClientCore.getInstance().getEventBus().subscribe(this, ClientTickEvent.class, this::onTick);
    }

    private void onTick(ClientTickEvent event) {
        if (event.getPhase() != ClientTickEvent.Phase.END || mc.options == null || mc.world == null) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now - lastUpdateMillis < 10_000L) {
            return;
        }
        int fps = PerformanceTracker.getInstance().getCurrentFps();
        int cur = mc.options.getSimulationDistance().getValue();
        int min = Math.min(minSimDistance.getValue(), maxSimDistance.getValue());
        int max = Math.max(minSimDistance.getValue(), maxSimDistance.getValue());

        if (fps < 55 && cur > min) {
            mc.options.getSimulationDistance().setValue(cur - 1);
            lastUpdateMillis = now;
        } else if (fps > 110 && cur < max) {
            mc.options.getSimulationDistance().setValue(cur + 1);
            lastUpdateMillis = now;
        }
    }
}
