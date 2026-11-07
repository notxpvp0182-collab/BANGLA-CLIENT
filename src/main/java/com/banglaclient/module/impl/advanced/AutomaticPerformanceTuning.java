package com.banglaclient.module.impl.advanced;

import com.banglaclient.core.ClientCore;
import com.banglaclient.core.PerformanceTracker;
import com.banglaclient.event.ClientTickEvent;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.IntegerSetting;
import net.minecraft.particle.ParticlesMode;

/**
 * Module 282: Automatic Performance Tuning
 * Dynamically adjusts particle density and biome blend radius when severe FPS drops occur.
 */
public final class AutomaticPerformanceTuning extends AbstractModule {

    private final IntegerSetting minAcceptableFps = registerSetting(new IntegerSetting(
            "auto_tune_min_fps", "Trigger FPS Floor", "Automatically reduce particle/blend workload below this FPS", 55, 25, 144, 5, " FPS"
    ));

    private long lastTuneTimestampMillis = 0L;

    public AutomaticPerformanceTuning() {
        super(
                "automatic_performance_tuning",
                "Automatic Performance Tuning",
                "Adjusts selected visual settings according to live performance.",
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
        if (now - lastTuneTimestampMillis < 6000L) {
            return;
        }
        int fps = PerformanceTracker.getInstance().getCurrentFps();
        int floor = minAcceptableFps.getValue();

        if (fps > 0 && fps < floor) {
            if (mc.options.getParticles().getValue() != ParticlesMode.MINIMAL) {
                mc.options.getParticles().setValue(ParticlesMode.MINIMAL);
                OptimizationLog.recordEntry("AutoTuning switched Particles -> MINIMAL (FPS: " + fps + ")");
            }
            lastTuneTimestampMillis = now;
        } else if (fps > floor + 35) {
            if (mc.options.getParticles().getValue() == ParticlesMode.MINIMAL) {
                mc.options.getParticles().setValue(ParticlesMode.DECREASED);
                OptimizationLog.recordEntry("AutoTuning restored Particles -> DECREASED (FPS: " + fps + ")");
            }
            lastTuneTimestampMillis = now;
        }
    }
}
