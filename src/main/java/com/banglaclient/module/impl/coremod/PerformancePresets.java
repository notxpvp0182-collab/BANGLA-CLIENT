package com.banglaclient.module.impl.coremod;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.profile.ProfileManager;
import com.banglaclient.setting.ActionSetting;
import com.banglaclient.setting.ModeSetting;

/**
 * Module 312: Performance Presets
 * Provides Low-End, Balanced, Quality, and Extreme FPS performance presets.
 */
public final class PerformancePresets extends AbstractModule {

    private final ModeSetting preset = registerSetting(new ModeSetting(
            "perf_preset_tier", "Performance Tier", "Choose a hardware performance preset",
            "Balanced", "Low-End", "Balanced", "Quality", "Extreme FPS"
    ));

    public PerformancePresets() {
        super(
                "performance_presets",
                "Performance Presets",
                "Provides Low-End, Balanced, Quality, and Extreme FPS presets.",
                ModuleCategory.BANGLACLIENT_CORE,
                false
        );

        registerSetting(new ActionSetting(
                "apply_perf_preset_btn",
                "Apply Selected Preset",
                "Activate the chosen performance preset",
                "Apply Preset",
                this::applySelected
        ));
    }

    @Override
    public void onEnable() {
        applySelected();
    }

    private void applySelected() {
        String id = switch (preset.getValue()) {
            case "Low-End" -> "low_end";
            case "Quality" -> "quality";
            case "Extreme FPS" -> "extreme_fps";
            default -> "balanced";
        };
        ProfileManager.getInstance().applyProfile(id);
    }
}
