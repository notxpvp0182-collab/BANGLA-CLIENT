package com.banglaclient.module.impl.coremod;

import com.banglaclient.config.ConfigManager;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.setting.ActionSetting;
import com.banglaclient.setting.ModeSetting;

/**
 * Module 314: PvP Presets
 * Provides specialized PvP configuration presets (Crystal / Mace PvP, Sword / SMP PvP, UHC / Pot PvP).
 */
public final class PvpPresets extends AbstractModule {

    private final ModeSetting pvpMode = registerSetting(new ModeSetting(
            "pvp_preset_mode", "Combat Kit Style", "Curated PvP module combination",
            "Crystal / Mace PvP", "Crystal / Mace PvP", "Sword / SMP PvP", "UHC / Potion PvP"
    ));

    public PvpPresets() {
        super(
                "pvp_presets",
                "PvP Presets",
                "Provides Crystal/Mace, Sword/SMP, and Potion PvP presets.",
                ModuleCategory.BANGLACLIENT_CORE,
                false
        );

        registerSetting(new ActionSetting(
                "apply_pvp_preset_btn",
                "Apply Combat Preset",
                "Enable the combat HUDs and optimizers for the chosen PvP style",
                "Apply PvP Kit",
                this::applyCombatPreset
        ));
    }

    public void applyCombatPreset() {
        ModuleRegistry reg = ModuleRegistry.getInstance();
        String kit = pvpMode.getValue();
        reg.getById("toggle_sprint").ifPresent(m -> m.setEnabled(true));
        reg.getById("low_fire").ifPresent(m -> m.setEnabled(true));
        reg.getById("small_shield").ifPresent(m -> m.setEnabled(true));
        reg.getById("small_totem").ifPresent(m -> m.setEnabled(true));
        reg.getById("armor_hud").ifPresent(m -> m.setEnabled(true));
        reg.getById("crystal_status_hud").ifPresent(m -> m.setEnabled(kit.equals("Crystal / Mace PvP")));
        reg.getById("totem_counter").ifPresent(m -> m.setEnabled(kit.equals("Crystal / Mace PvP") || kit.equals("Sword / SMP PvP")));
        reg.getById("potion_hud").ifPresent(m -> m.setEnabled(true));
        ConfigManager.getInstance().saveConfig();
    }
}
