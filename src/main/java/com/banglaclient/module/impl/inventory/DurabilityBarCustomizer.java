package com.banglaclient.module.impl.inventory;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.ColorSetting;
import com.banglaclient.setting.ModeSetting;
import com.banglaclient.theme.ColorData;

/**
 * Module 202: Durability Bar Customizer
 * Customizes inventory slot durability bar appearance (Theme Red, Smooth HSB, or Custom Color).
 */
public final class DurabilityBarCustomizer extends AbstractModule {

    private final ModeSetting colorMode = registerSetting(new ModeSetting(
            "dura_bar_mode", "Color Mode", "Coloring style for item durability bars",
            "Theme Accent", "Vanilla HSB", "Theme Accent", "Custom Color"
    ));
    private final ColorSetting customBarColor = registerSetting(new ColorSetting(
            "custom_dura_color", "Custom Bar Color", "Fixed color when using Custom Color mode", ColorData.RED_ACCENT, false
    ));
    private final BooleanSetting alwaysShowFullBar = registerSetting(new BooleanSetting(
            "always_show_full_bar", "Show At 100% Durability", "Render durability bar even on undamaged tools", false
    ));

    public DurabilityBarCustomizer() {
        super(
                "durability_bar_customizer",
                "Durability Bar Customizer",
                "Customizes item durability bar colors and visibility.",
                ModuleCategory.INVENTORY_ITEMS,
                false
        );
        customBarColor.visibleWhen(() -> colorMode.isMode("Custom Color"));
    }

    public String getColorMode() { return colorMode.getValue(); }
    public ColorData getCustomBarColor() { return customBarColor.getValue(); }
    public boolean isAlwaysShowFullBar() { return isEnabled() && alwaysShowFullBar.isEnabled(); }
}
