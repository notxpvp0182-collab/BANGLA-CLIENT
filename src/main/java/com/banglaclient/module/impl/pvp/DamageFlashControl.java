package com.banglaclient.module.impl.pvp;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ColorSetting;
import com.banglaclient.setting.FloatSetting;
import com.banglaclient.theme.ColorData;

/**
 * Module 114: Damage Flash Control
 * Customizes the entity hurt overlay tint color and opacity when an entity takes damage.
 */
public final class DamageFlashControl extends AbstractModule {

    private final ColorSetting flashColor = registerSetting(new ColorSetting(
            "damage_flash_color", "Flash Color", "Tint color applied to entities taking damage", new ColorData(229, 37, 33, 140), true
    ));
    private final FloatSetting intensityMultiplier = registerSetting(new FloatSetting(
            "flash_intensity", "Flash Intensity", "Opacity multiplier for the hurt overlay", 1.0f, 0.0f, 1.5f, 0.05f, "x"
    ));

    public DamageFlashControl() {
        super(
                "damage_flash_control",
                "Damage Flash Control",
                "Customizes the entity hurt overlay color and opacity.",
                ModuleCategory.PVP_COMBAT,
                false
        );
    }

    public ColorData getEffectiveFlashColor() {
        return flashColor.getValue().withAlphaFactor(Math.min(1.0f, intensityMultiplier.getValue()));
    }
}
