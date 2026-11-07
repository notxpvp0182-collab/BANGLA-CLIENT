package com.banglaclient.module.impl.hud;

import com.banglaclient.config.ConfigManager;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.setting.ActionSetting;
import com.banglaclient.setting.ModeSetting;

/**
 * Module 126: HUD Presets
 * Allows saving and loading curated HUD visibility sets while preserving continuous coordinates.
 */
public final class HudPresets extends AbstractModule {

    private final ModeSetting presetChoice = registerSetting(new ModeSetting(
            "hud_preset_choice", "HUD Preset", "Curated HUD module visibility preset",
            "Minimal FPS", "Minimal FPS", "Competitive PvP", "Survival Explorer", "All Telemetry"
    ));

    public HudPresets() {
        super(
                "hud_presets",
                "HUD Presets",
                "Allows saving and loading curated HUD layouts.",
                ModuleCategory.HUD_INFORMATION,
                false
        );

        registerSetting(new ActionSetting(
                "apply_hud_preset",
                "Load Selected Preset",
                "Enable the HUD modules associated with the chosen preset",
                "Load HUD Preset",
                this::applySelectedPreset
        ));
    }

    private void applySelectedPreset() {
        String mode = presetChoice.getValue();
        for (HudModule hud : ModuleRegistry.getInstance().getAllHudModules()) {
            String id = hud.getId();
            boolean enable = switch (mode) {
                case "Competitive PvP" -> id.equals("fps_display") || id.equals("cps_counter") || id.equals("armor_hud")
                        || id.equals("potion_hud") || id.equals("totem_counter") || id.equals("reach_display");
                case "Survival Explorer" -> id.equals("fps_display") || id.equals("coordinates_hud") || id.equals("direction_hud")
                        || id.equals("biome_display") || id.equals("real_time_clock") || id.equals("armor_hud");
                case "All Telemetry" -> id.equals("fps_display") || id.equals("one_percent_low_fps") || id.equals("frame_time_display")
                        || id.equals("cpu_usage_hud") || id.equals("ram_usage_hud") || id.equals("ping_display");
                default -> id.equals("fps_display") || id.equals("coordinates_hud");
            };
            hud.setEnabled(enable);
        }
        ConfigManager.getInstance().saveConfig();
    }
}
