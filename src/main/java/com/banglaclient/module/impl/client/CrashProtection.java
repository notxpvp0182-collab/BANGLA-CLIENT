package com.banglaclient.module.impl.client;

import com.banglaclient.core.BanglaLogger;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 278: Crash Protection
 * Detects conflicting configuration patterns (such as enabling both Dynamic Render Distance and
 * Smart Render Distance simultaneously, or conflicting FPS limiters) and resolves them safely.
 */
public final class CrashProtection extends AbstractModule {

    private final BooleanSetting autoResolveLimiterConflicts = registerSetting(new BooleanSetting(
            "auto_resolve_conflicts", "Auto-Resolve Conflicts", "Prevent competing dynamic render distance modules from fighting", true
    ));

    public CrashProtection() {
        super(
                "crash_protection",
                "Crash Protection",
                "Detects and prevents conflicting configuration patterns.",
                ModuleCategory.CLIENT_SETTINGS,
                true
        );
    }

    public void validateConfigurationSafety() {
        if (!isEnabled() || !autoResolveLimiterConflicts.isEnabled()) {
            return;
        }
        var dynDist = ModuleRegistry.getInstance().getById("dynamic_render_distance");
        var smartDist = ModuleRegistry.getInstance().getById("smart_render_distance");
        if (dynDist.isPresent() && smartDist.isPresent() && dynDist.get().isEnabled() && smartDist.get().isEnabled()) {
            smartDist.get().setEnabled(false);
            BanglaLogger.warn("CrashProtection disabled Smart Render Distance because Dynamic Render Distance is already active.");
        }
    }
}
