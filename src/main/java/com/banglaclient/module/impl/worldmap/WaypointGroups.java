package com.banglaclient.module.impl.worldmap;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.ModeSetting;

/**
 * Module 247: Waypoint Groups
 * Organizes waypoints into logical groups (Bases, Farms, Portals, PvP Arenas, Deaths).
 */
public final class WaypointGroups extends AbstractModule {

    private final ModeSetting activeGroupFilter = registerSetting(new ModeSetting(
            "active_wp_group", "Active Group Filter", "Filter displayed waypoints by category group",
            "All Groups", "All Groups", "General", "Bases", "Farms", "Portals", "Deaths"
    ));

    public WaypointGroups() {
        super(
                "waypoint_groups",
                "Waypoint Groups",
                "Organizes waypoints into Bases, Farms, Portals, and Deaths.",
                ModuleCategory.WORLD_MAP,
                true
        );
    }

    public String getActiveGroupFilter() {
        return activeGroupFilter.getValue();
    }
}
