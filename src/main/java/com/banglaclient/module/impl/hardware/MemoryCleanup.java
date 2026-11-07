package com.banglaclient.module.impl.hardware;

import com.banglaclient.core.BanglaLogger;
import com.banglaclient.core.HardwareTelemetry;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ActionSetting;
import com.banglaclient.util.ClientResourceManager;

/**
 * Module 075: Memory Cleanup
 * Provides safe cleanup of unused client caches and triggers JVM garbage collection hint
 * with a rate-limiter so users cannot accidentally freeze the game by spamming GC.
 */
public final class MemoryCleanup extends AbstractModule {

    private static long lastCleanupTimestampMillis = 0L;
    private static final long MIN_CLEANUP_INTERVAL_MS = 10_000L;

    public MemoryCleanup() {
        super(
                "memory_cleanup",
                "Memory Cleanup",
                "Provides safe manual cleanup of unused client resources.",
                ModuleCategory.RAM_CPU_GPU,
                true
        );

        registerSetting(new ActionSetting(
                "run_cleanup_now",
                "Execute Cleanup",
                "Safely clear unused client caches and request JVM heap cleanup",
                "Clean Memory Now",
                MemoryCleanup::performSafeCleanup
        ));
    }

    public static boolean performSafeCleanup() {
        long now = System.currentTimeMillis();
        if (now - lastCleanupTimestampMillis < MIN_CLEANUP_INTERVAL_MS) {
            return false;
        }
        lastCleanupTimestampMillis = now;

        long beforeMb = HardwareTelemetry.getInstance().getUsedMemoryMb();
        ClientResourceManager.invalidateCache();
        System.gc();
        HardwareTelemetry.getInstance().pollMetrics();
        long afterMb = HardwareTelemetry.getInstance().getUsedMemoryMb();
        long freed = Math.max(0L, beforeMb - afterMb);
        BanglaLogger.info("Executed safe memory cleanup (Freed ~{} MB).", freed);
        return true;
    }
}
