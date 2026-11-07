package com.banglaclient.module.impl.coremod;

import com.banglaclient.config.ConfigManager;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.setting.ActionSetting;
import com.banglaclient.setting.ModeSetting;

/**
 * Module 313: Visual Presets
 * Provides curated visual presets (Clean Competitive, Cave Explorer, Cinematic Atmosphere).
 */
public final class VisualPresets extends AbstractModule {

    private final ModeSetting visualStyle = registerSetting(new ModeSetting(
            "visual_preset_style", "Visual Style", "Curated graphics and world-visual preset",
            "Clean Competitive", "Clean Competitive", "Cave Explorer", "Cinematic Atmosphere"
    ));

    public VisualPresets() {
        super(
                "visual_presets",
                "Visual Presets",
                "Provides curated visual and graphics configuration presets.",
                ModuleCategory.BANGLACLIENT_CORE,
                false
        );

        registerSetting(new ActionSetting(
                "apply_visual_preset_btn",
                "Apply Visual Preset",
                "Configure visual modules according to the selected preset",
                "Apply Visuals",
                this::applyVisualPreset
        ));
    }

    public void applyVisualPreset() {
        ModuleRegistry reg = ModuleRegistry.getInstance();
        String style = visualStyle.getValue();
        reg.getById("fullbright").ifPresent(m -> m.setEnabled(!style.equals("Cinematic Atmosphere")));
        reg.getById("low_fire").ifPresent(m -> m.setEnabled(style.equals("Clean Competitive")));
        reg.getById("no_fog").ifPresent(m -> m.setEnabled(style.equals("Clean Competitive") || style.equals("Cave Explorer")));
        reg.getById("clear_water").ifPresent(m -> m.setEnabled(true));
        ConfigManager.getInstance().saveConfig();
    }
}
