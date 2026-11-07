package com.banglaclient.module.impl.hardware;

import com.banglaclient.core.ClientCore;
import com.banglaclient.core.HardwareTelemetry;
import com.banglaclient.event.ClientTickEvent;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 077: Smart Memory Cleanup
 * Runs safe cache cleanup automatically when JVM heap usage exceeds a configured percentage
 * and the player is not in the middle of active combat.
 */
public final class SmartMemoryCleanup extends AbstractModule {

    private final IntegerSetting heapThresholdPercent = registerSetting(new IntegerSetting(
            "heap_threshold_pct", "Heap Usage Trigger", "Trigger cleanup when RAM usage exceeds this percentage", 85, 65, 95, 5, "%"
    ));
    private final BooleanSetting pauseDuringCombat = registerSetting(new BooleanSetting(
            "pause_during_combat", "Never Clean In Combat", "Defer cleanup while swinging weapons or taking damage", true
    ));

    private long lastAutoCheckMillis = 0L;

    public SmartMemoryCleanup() {
        super(
                "smart_memory_cleanup",
                "Smart Memory Cleanup",
                "Runs memory cleanup automatically when RAM usage is high.",
                ModuleCategory.RAM_CPU_GPU,
                true
        );
    }

    @Override
    public void onEnable() {
        ClientCore.getInstance().getEventBus().subscribe(this, ClientTickEvent.class, this::onTick);
    }

    private void onTick(ClientTickEvent event) {
        if (event.getPhase() != ClientTickEvent.Phase.END) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now - lastAutoCheckMillis < 15_000L) {
            return;
        }
        lastAutoCheckMillis = now;

        if (pauseDuringCombat.isEnabled() && mc.player != null && (mc.player.handSwinging || mc.player.hurtTime > 0)) {
            return;
        }

        HardwareTelemetry hw = HardwareTelemetry.getInstance();
        hw.pollMetrics();
        if (hw.getMemoryUsagePercent() >= heapThresholdPercent.getValue()) {
            MemoryCleanup.performSafeCleanup();
        }
    }
}
