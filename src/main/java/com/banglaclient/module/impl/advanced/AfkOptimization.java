package com.banglaclient.module.impl.advanced;

import com.banglaclient.core.ClientCore;
import com.banglaclient.event.ClientTickEvent;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 288: AFK Optimization
 * Automatically detects when the player has not moved or rotated the camera for a configurable
 * number of seconds and lowers the frame rate to save power while AFK at farms.
 */
public final class AfkOptimization extends AbstractModule {

    private final IntegerSetting afkTimeoutSeconds = registerSetting(new IntegerSetting(
            "afk_timeout_sec", "AFK Detection Time", "Seconds without player movement before entering AFK mode", 90, 30, 600, 30, "s"
    ));
    private final IntegerSetting afkFpsLimit = registerSetting(new IntegerSetting(
            "afk_fps_limit", "AFK FPS Cap", "Frame rate cap while AFK", 20, 5, 60, 5, " FPS"
    ));

    private long lastActivityMillis = System.currentTimeMillis();
    private float lastYaw = 0.0f;
    private float lastPitch = 0.0f;
    private boolean currentlyAfk = false;

    public AfkOptimization() {
        super(
                "afk_optimization",
                "AFK Optimization",
                "Reduces unnecessary rendering workload while the player is AFK.",
                ModuleCategory.ADVANCED_PERFORMANCE,
                true
        );
    }

    @Override
    public void onEnable() {
        lastActivityMillis = System.currentTimeMillis();
        ClientCore.getInstance().getEventBus().subscribe(this, ClientTickEvent.class, this::onTick);
    }

    private void onTick(ClientTickEvent event) {
        if (event.getPhase() != ClientTickEvent.Phase.END || mc.player == null) {
            return;
        }
        float yaw = mc.player.getYaw();
        float pitch = mc.player.getPitch();
        if (Math.abs(yaw - lastYaw) > 0.05f || Math.abs(pitch - lastPitch) > 0.05f
                || mc.player.getVelocity().horizontalLengthSquared() > 0.0002
                || mc.player.handSwinging) {
            lastYaw = yaw;
            lastPitch = pitch;
            lastActivityMillis = System.currentTimeMillis();
            currentlyAfk = false;
            return;
        }

        long idleSec = (System.currentTimeMillis() - lastActivityMillis) / 1000L;
        currentlyAfk = idleSec >= afkTimeoutSeconds.getValue();
    }

    public boolean isCurrentlyAfk() {
        return isEnabled() && currentlyAfk;
    }

    public int getAfkFpsLimit() {
        return afkFpsLimit.getValue();
    }
}
