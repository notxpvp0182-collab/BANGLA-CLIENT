package com.banglaclient.module.impl.visual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ColorSetting;
import com.banglaclient.setting.FloatSetting;
import com.banglaclient.theme.ColorData;

/**
 * Module 184: Dark GUI Mode
 * Styles vanilla container screens (Inventory, Chests, Crafting, Anvil) with a sleek dark translucent backdrop.
 */
public final class DarkGuiMode extends AbstractModule {

    private final FloatSetting backdropDarkness = registerSetting(new FloatSetting(
            "backdrop_darkness", "Backdrop Opacity", "Darkness level behind open container screens", 0.75f, 0.25f, 0.95f, 0.05f, ""
    ));
    private final ColorSetting containerTint = registerSetting(new ColorSetting(
            "container_tint", "Container Tint", "Dark tint applied to vanilla container panels", new ColorData(22, 22, 28, 255), false
    ));

    public DarkGuiMode() {
        super(
                "dark_gui_mode",
                "Dark GUI Mode",
                "Provides dark GUI styling for vanilla inventory and containers.",
                ModuleCategory.VISUAL_GRAPHICS,
                false
        );
    }

    public float getBackdropDarkness() { return isEnabled() ? backdropDarkness.getValue() : 0.5f; }
    public ColorData getContainerTint() { return containerTint.getValue(); }
}
