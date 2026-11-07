package com.banglaclient.module.impl.worldmap;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 244: Waypoint Coordinates
 * Controls whether exact X/Y/Z block coordinates and Nether/Overworld 8:1 translated coordinates
 * are displayed on waypoints.
 */
public final class WaypointCoordinates extends AbstractModule {

    private final BooleanSetting showExactXyz = registerSetting(new BooleanSetting(
            "show_wp_xyz", "Show X/Y/Z Coordinates", "Display exact block coordinates on waypoints", true
    ));
    private final BooleanSetting showNetherConversion = registerSetting(new BooleanSetting(
            "show_nether_conversion", "Show 8:1 Nether Coords", "Calculate corresponding Nether/Overworld portal coordinates", true
    ));

    public WaypointCoordinates() {
        super(
                "waypoint_coordinates",
                "Waypoint Coordinates",
                "Displays waypoint X/Y/Z and Nether-converted coordinates.",
                ModuleCategory.WORLD_MAP,
                true
        );
    }

    public boolean isShowExactXyz() { return isEnabled() && showExactXyz.isEnabled(); }
    public boolean isShowNetherConversion() { return isEnabled() && showNetherConversion.isEnabled(); }
}
