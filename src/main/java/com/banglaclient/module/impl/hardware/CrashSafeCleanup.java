package com.banglaclient.module.impl.hardware;

import com.banglaclient.config.ConfigManager;
import com.banglaclient.core.BanglaLogger;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 087: Crash-Safe Cleanup
 * Registers a JVM shutdown hook to safely flush configuration state and release client resources
 * even during abnormal termination.
 */
public final class CrashSafeCleanup extends AbstractModule {

    private final BooleanSetting saveConfigOnAbnormalExit = registerSetting(new BooleanSetting(
            "save_on_abnormal_exit", "Emergency Config Flush", "Flush configuration atomically via JVM shutdown hook", true
    ));

    private static boolean hookRegistered = false;

    public CrashSafeCleanup() {
        super(
                "crash_safe_cleanup",
                "Crash-Safe Cleanup",
                "Safely handles client resources during abnormal shutdown.",
                ModuleCategory.RAM_CPU_GPU,
                true
        );
        registerShutdownHookOnce();
    }

    private void registerShutdownHookOnce() {
        if (hookRegistered) {
            return;
        }
        hookRegistered = true;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (isRawEnabled() && saveConfigOnAbnormalExit.isEnabled()) {
                try {
                    ConfigManager.getInstance().saveConfig();
                } catch (Throwable t) {
                    BanglaLogger.error("Emergency shutdown config flush encountered an error", t);
                }
            }
        }, "BanglaClient-CrashSafeHook"));
    }
}
