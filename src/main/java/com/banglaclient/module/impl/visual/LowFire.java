package com.banglaclient.module.impl.visual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 181: Low Fire
 * Reduces the vertical height and opacity of the first-person burning fire overlay so your view is not obstructed in PvP.
 */
public final class LowFire extends AbstractModule {

    private final FloatSetting verticalOffset = registerSetting(new FloatSetting(
            "low_fire_offset", "Vertical Drop", "How far down to shift the first-person fire overlay", -0.32f, -0.55f, 0.0f, 0.02f, ""
    ));
    private final FloatSetting fireOpacity = registerSetting(new FloatSetting(
            "low_fire_opacity", "Fire Opacity", "Opacity multiplier for the burning screen overlay", 0.75f, 0.15f, 1.0f, 0.05f, ""
    ));
    private final BooleanSetting hideCompletelyWithFireRes = registerSetting(new BooleanSetting(
            "hide_with_fire_res", "Hide With Fire Res", "Completely hide first-person fire when Fire Resistance is active", false
    ));

    public LowFire() {
        super(
                "low_fire",
                "Low Fire",
                "Reduces first-person fire overlay height for clear PvP vision.",
                ModuleCategory.VISUAL_GRAPHICS,
                true
        );
    }

    public float getVerticalOffset() { return isEnabled() ? verticalOffset.getValue() : 0.0f; }
    public float getFireOpacity() { return isEnabled() ? fireOpacity.getValue() : 1.0f; }
    public boolean isHideCompletelyWithFireRes() { return isEnabled() && hideCompletelyWithFireRes.isEnabled(); }
}
