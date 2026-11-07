package com.banglaclient.module.impl.overlay;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.ColorSetting;
import com.banglaclient.setting.FloatSetting;
import com.banglaclient.theme.ColorData;

/**
 * Module 255: Custom Tablist
 * Customizes tablist layout, scale, numeric ping readout, and dark/red header styling.
 */
public final class CustomTablist extends AbstractModule {

    private final FloatSetting tablistScale = registerSetting(new FloatSetting(
            "tablist_scale", "Tablist Scale", "Size multiplier for the multiplayer tablist", 1.0f, 0.5f, 1.25f, 0.05f, "x"
    ));
    private final BooleanSetting showNumericPing = registerSetting(new BooleanSetting(
            "show_numeric_ping", "Numeric Ping (ms)", "Display exact latency in ms instead of 5 signal bars", true
    ));
    private final BooleanSetting hideHeaderFooter = registerSetting(new BooleanSetting(
            "hide_header_footer", "Compact (No Header/Footer)", "Hide oversized server advertisement headers/footers", false
    ));
    private final ColorSetting backgroundColor = registerSetting(new ColorSetting(
            "tablist_bg_color", "Background Color", "Tablist backdrop color", new ColorData(14, 14, 18, 210), true
    ));

    public CustomTablist() {
        super(
                "custom_tablist",
                "Custom Tablist",
                "Customizes tablist layout, scale, and numeric ping.",
                ModuleCategory.SCOREBOARD_TABLIST_NAMETAG,
                true
        );
    }

    public float getTablistScale() { return isEnabled() ? tablistScale.getValue() : 1.0f; }
    public boolean isShowNumericPing() { return isEnabled() && showNumericPing.isEnabled(); }
    public boolean isHideHeaderFooter() { return isEnabled() && hideHeaderFooter.isEnabled(); }
    public ColorData getBackgroundColor() { return backgroundColor.getValue(); }
}
