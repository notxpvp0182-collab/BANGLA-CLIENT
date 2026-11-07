package com.banglaclient.module.impl.performance;

import com.banglaclient.core.ClientCore;
import com.banglaclient.event.ClientTickEvent;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 063: Adaptive FPS Limiter
 * Dynamically adjusts the client FPS cap between active combat/movement and stationary idle states
 * to keep hardware cool without sacrificing responsiveness.
 */
public final class AdaptiveFpsLimiter extends AbstractModule {

    private final IntegerSetting activeFpsCap = registerSetting(new IntegerSetting(
            "active_fps_cap", "Active FPS Cap", "Maximum FPS while moving or fighting", 240, 60, 260, 10, " FPS"
    ));
    private final IntegerSetting stationaryFpsCap = registerSetting(new IntegerSetting(
            "stationary_fps_cap", "Stationary FPS Cap", "Reduced FPS cap after standing still for 10 seconds", 90, 30, 144, 10, " FPS"
    ));

    private long lastMovementMillis = System.currentTimeMillis();

    public AdaptiveFpsLimiter() {
        super(
                "adaptive_fps_limiter",
                "Adaptive FPS Limiter",
                "Adjusts FPS limit according to player activity.",
                ModuleCategory.PERFORMANCE_FPS,
                false
        );
    }

    @Override
    public void onEnable() {
        ClientCore.getInstance().getEventBus().subscribe(this, ClientTickEvent.class, this::onTick);
    }

    private void onTick(ClientTickEvent event) {
        if (event.getPhase() != ClientTickEvent.Phase.END || mc.player == null || mc.options == null) {
            return;
        }
        long now = System.currentTimeMillis();
        if (mc.player.getVelocity().horizontalLengthSquared() > 0.0005 || mc.player.handSwinging) {
            lastMovementMillis = now;
        }
        int targetCap = (now - lastMovementMillis > 10_000L) ? stationaryFpsCap.getValue() : activeFpsCap.getValue();
        if (mc.options.getMaxFps().getValue() != targetCap) {
            mc.options.getMaxFps().setValue(targetCap);
        }
    }
}
