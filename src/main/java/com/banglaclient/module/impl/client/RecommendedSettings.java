package com.banglaclient.module.impl.client;

import com.banglaclient.core.HardwareTelemetry;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.profile.ProfileManager;
import com.banglaclient.setting.ActionSetting;

/**
 * Module 271: Recommended Settings
 * Analyzes available CPU threads and allocated JVM RAM to suggest and apply the ideal profile.
 */
public final class RecommendedSettings extends AbstractModule {

    public RecommendedSettings() {
        super(
                "recommended_settings",
                "Recommended Settings",
                "Suggests and applies optimal settings based on your hardware.",
                ModuleCategory.CLIENT_SETTINGS,
                false
        );

        registerSetting(new ActionSetting(
                "apply_hw_recommended",
                "Apply Hardware Recommendation",
                "Auto-select Low-End, Balanced, or Quality profile based on your system specs",
                "Auto-Configure",
                this::applyHardwareRecommendation
        ));
    }

    public String getRecommendedTier() {
        HardwareTelemetry hw = HardwareTelemetry.getInstance();
        int cores = hw.getAvailableProcessors();
        long maxRam = hw.getMaxMemoryMb();
        if (cores <= 4 || maxRam <= 2048L) {
            return "low_end";
        }
        if (cores >= 12 && maxRam >= 6144L) {
            return "quality";
        }
        return "balanced";
    }

    public void applyHardwareRecommendation() {
        ProfileManager.getInstance().applyProfile(getRecommendedTier());
    }
}
