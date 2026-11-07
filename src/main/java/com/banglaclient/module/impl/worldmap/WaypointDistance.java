package com.banglaclient.module.impl.worldmap;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 245: Waypoint Distance
 * Displays Euclidean or horizontal distance in meters to each saved waypoint.
 */
public final class WaypointDistance extends AbstractModule {

    private final BooleanSetting horizontalOnly = registerSetting(new BooleanSetting(
            "horizontal_dist_only", "Ignore Y-Altitude", "Measure 2D horizontal X/Z distance instead of 3D Euclidean distance", false
    ));
    private final IntegerSetting maxVisibleDistance = registerSetting(new IntegerSetting(
            "max_wp_visible_dist", "Max Render Distance", "Hide waypoints further than this distance (5000 = Unlimited)", 5000, 100, 5000, 100, "m"
    ));

    public WaypointDistance() {
        super(
                "waypoint_distance",
                "Waypoint Distance",
                "Displays distance in meters to saved waypoints.",
                ModuleCategory.WORLD_MAP,
                true
        );
    }

    public boolean isHorizontalOnly() { return isEnabled() && horizontalOnly.isEnabled(); }
    public int getMaxVisibleDistance() { return maxVisibleDistance.getValue(); }
}
