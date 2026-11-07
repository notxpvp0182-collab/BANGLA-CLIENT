package com.banglaclient.module.impl.visual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 180: Clear Lava
 * Improves visibility when submerged in lava with Fire Resistance active.
 */
public final class ClearLava extends AbstractModule {

    private final FloatSetting lavaViewDistance = registerSetting(new FloatSetting(
            "lava_view_dist", "Lava Fog End", "Fog end distance in blocks when submerged in lava", 16.0f, 3.0f, 48.0f, 1.0f, "m"
    ));

    public ClearLava() {
        super(
                "clear_lava",
                "Clear Lava",
                "Improves visibility when submerged in lava.",
                ModuleCategory.VISUAL_GRAPHICS,
                false
        );
    }

    public float getLavaViewDistance() {
        return isEnabled() ? lavaViewDistance.getValue() : 3.0f;
    }
}
