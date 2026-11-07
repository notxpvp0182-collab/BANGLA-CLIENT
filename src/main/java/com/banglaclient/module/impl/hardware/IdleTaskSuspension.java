package com.banglaclient.module.impl.hardware;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 084: Idle Task Suspension
 * Pauses compatible non-essential client polling and graph sampling tasks when the game window is minimized or idle.
 */
public final class IdleTaskSuspension extends AbstractModule {

    private final IntegerSetting idleTimeoutSeconds = registerSetting(new IntegerSetting(
            "idle_timeout_sec", "Idle Detection Time", "Seconds without input before suspending non-essential tasks", 60, 15, 300, 15, "s"
    ));
    private final BooleanSetting suspendWhenMinimized = registerSetting(new BooleanSetting(
            "suspend_when_minimized", "Suspend When Minimized", "Immediately pause telemetry graphs when window is minimized", true
    ));

    private long lastInputTimestampMillis = System.currentTimeMillis();

    public IdleTaskSuspension() {
        super(
                "idle_task_suspension",
                "Idle Task Suspension",
                "Pauses compatible non-essential tasks when idle or minimized.",
                ModuleCategory.RAM_CPU_GPU,
                true
        );
    }

    public void notifyUserActivity() {
        this.lastInputTimestampMillis = System.currentTimeMillis();
    }

    public boolean isClientIdle() {
        if (!isEnabled()) {
            return false;
        }
        if (suspendWhenMinimized.isEnabled() && mc.getWindow() != null && !mc.isWindowFocused()) {
            return true;
        }
        long elapsedSeconds = (System.currentTimeMillis() - lastInputTimestampMillis) / 1000L;
        return elapsedSeconds >= idleTimeoutSeconds.getValue();
    }
}
