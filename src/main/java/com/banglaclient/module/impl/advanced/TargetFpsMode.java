package com.banglaclient.module.impl.advanced;

import com.banglaclient.core.ClientCore;
import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.event.ClientTickEvent;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 283: Target FPS Mode
 * Dynamically steps entity distance scaling and simulation distance to maintain a chosen target FPS.
 */
public final class TargetFpsMode extends AbstractModule {

    private final IntegerSetting desiredFps = registerSetting(new IntegerSetting(
            "target_fps_goal", "Target FPS Goal", "Desired frame rate to maintain", 120, 45, 240, 5, " FPS"
    ));

    private long lastStepMillis = 0L;

    public TargetFpsMode() {
        super(
                "target_fps_mode",
                "Target FPS Mode",
                "Dynamically adjusts entity distance scaling to maintain target FPS.",
                ModuleCategory.ADVANCED_PERFORMANCE,
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
        if (now - lastStepMillis < 5000L) {
            return;
        }
        int fps = PerformanceTracker.getInstance().getCurrentFps();
        int goal = desiredFps.getValue();
        double currentEntityDist = mc.options.getEntityDistanceScaling().getValue();

        if (fps > 0 && fps < goal - 15 && currentEntityDist > 0.5) {
            double next = Math.max(0.5, currentEntityDist - 0.25);
            mc.options.getEntityDistanceScaling().setValue(next);
            OptimizationLog.recordEntry(String.format("TargetFPS stepped EntityScale -> %.0f%%", next * 100.0));
            lastStepMillis = now;
        } else if (fps > goal + 25 && currentEntityDist < 1.0) {
            double next = Math.min(1.0, currentEntityDist + 0.25);
            mc.options.getEntityDistanceScaling().setValue(next);
            lastStepMillis = now;
        }
    }
}
