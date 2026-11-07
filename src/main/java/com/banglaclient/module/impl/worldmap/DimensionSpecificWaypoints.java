package com.banglaclient.module.impl.worldmap;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 248: Dimension-Specific Waypoints
 * Automatically separates Overworld, Nether, and End waypoints so only relevant dimension markers are shown.
 */
public final class DimensionSpecificWaypoints extends AbstractModule {

    private final BooleanSetting autoTranslateNetherOverworld = registerSetting(new BooleanSetting(
            "auto_translate_nether", "Show 8:1 Overworld Bridges", "Show scaled Overworld waypoints when travelling in the Nether", true
    ));

    public DimensionSpecificWaypoints() {
        super(
                "dimension_specific_waypoints",
                "Dimension-Specific Waypoints",
                "Separates waypoints automatically by Overworld, Nether, and End.",
                ModuleCategory.WORLD_MAP,
                true
        );
    }

    public boolean isAutoTranslateNetherOverworld() {
        return isEnabled() && autoTranslateNetherOverworld.isEnabled();
    }
}
