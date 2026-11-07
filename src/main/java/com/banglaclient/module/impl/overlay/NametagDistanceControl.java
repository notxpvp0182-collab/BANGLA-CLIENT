package com.banglaclient.module.impl.overlay;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 258: Nametag Distance Control
 * Controls maximum render distance for player and entity nametags (up to vanilla's 64m limit).
 */
public final class NametagDistanceControl extends AbstractModule {

    private final IntegerSetting maxDistance = registerSetting(new IntegerSetting(
            "nametag_max_distance", "Render Distance", "Maximum distance in blocks to render nametags", 48, 8, 64, 4, "m"
    ));

    public NametagDistanceControl() {
        super(
                "nametag_distance_control",
                "Nametag Distance Control",
                "Controls maximum nametag render distance.",
                ModuleCategory.SCOREBOARD_TABLIST_NAMETAG,
                false
        );
    }

    public double getMaxSquaredDistance() {
        int d = isEnabled() ? maxDistance.getValue() : 64;
        return (double) (d * d);
    }
}
