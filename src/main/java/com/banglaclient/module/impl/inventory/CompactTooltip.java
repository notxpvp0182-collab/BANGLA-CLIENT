package com.banglaclient.module.impl.inventory;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 205: Compact Tooltip
 * Makes large item tooltips more compact so they never overflow off the bottom of the screen.
 */
public final class CompactTooltip extends AbstractModule {

    private final FloatSetting tooltipScale = registerSetting(new FloatSetting(
            "compact_tooltip_scale", "Tooltip Scale", "Scale multiplier for large item tooltips", 0.85f, 0.5f, 1.0f, 0.05f, "x"
    ));
    private final BooleanSetting clampToScreenBounds = registerSetting(new BooleanSetting(
            "clamp_to_screen", "Prevent Screen Overflow", "Shift tall tooltips upward so bottom lines are never cut off", true
    ));

    public CompactTooltip() {
        super(
                "compact_tooltip",
                "Compact Tooltip",
                "Makes tooltips compact and prevents screen-edge overflow.",
                ModuleCategory.INVENTORY_ITEMS,
                false
        );
    }

    public float getTooltipScale() { return isEnabled() ? tooltipScale.getValue() : 1.0f; }
    public boolean isClampToScreenBounds() { return isEnabled() && clampToScreenBounds.isEnabled(); }
}
