package com.banglaclient.module.impl.worldmap;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ColorSetting;
import com.banglaclient.theme.ColorData;

/**
 * Module 246: Waypoint Colors
 * Configures default and group-specific colors for newly created waypoints.
 */
public final class WaypointColors extends AbstractModule {

    private final ColorSetting defaultWaypointColor = registerSetting(new ColorSetting(
            "default_wp_color", "Default Waypoint Color", "Color assigned to newly created waypoints", ColorData.RED_ACCENT, true
    ));
    private final ColorSetting deathWaypointColor = registerSetting(new ColorSetting(
            "death_wp_color", "Death Waypoint Color", "Color assigned to automatic death waypoints", new ColorData(255, 65, 54, 255), true
    ));

    public WaypointColors() {
        super(
                "waypoint_colors",
                "Waypoint Colors",
                "Allows custom RGB colors for waypoints and death markers.",
                ModuleCategory.WORLD_MAP,
                true
        );
    }

    public ColorData getDefaultWaypointColor() { return defaultWaypointColor.getValue(); }
    public ColorData getDeathWaypointColor() { return deathWaypointColor.getValue(); }
}
