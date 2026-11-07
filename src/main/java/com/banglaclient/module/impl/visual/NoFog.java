package com.banglaclient.module.impl.visual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 177: No Fog
 * Disables terrain, Nether, water, and lava distance fog for clear visibility.
 */
public final class NoFog extends AbstractModule {

    private final BooleanSetting disableTerrainFog = registerSetting(new BooleanSetting(
            "disable_terrain_fog", "Remove Terrain & Nether Fog", "Push horizon fog far beyond render distance", true
    ));
    private final BooleanSetting disableLiquidFog = registerSetting(new BooleanSetting(
            "disable_liquid_fog", "Reduce Water/Lava Fog", "Improve visibility when submerged in liquids", true
    ));

    public NoFog() {
        super(
                "no_fog",
                "No Fog",
                "Disables terrain and Nether distance fog.",
                ModuleCategory.VISUAL_GRAPHICS,
                false
        );
    }

    public boolean shouldDisableTerrainFog() {
        return isEnabled() && disableTerrainFog.isEnabled();
    }

    public boolean shouldDisableLiquidFog() {
        return isEnabled() && disableLiquidFog.isEnabled();
    }
}
