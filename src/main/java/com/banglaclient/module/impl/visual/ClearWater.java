package com.banglaclient.module.impl.visual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 179: Clear Water
 * Improves underwater visibility by extending the underwater fog end distance.
 */
public final class ClearWater extends AbstractModule {

    private final FloatSetting clarityMultiplier = registerSetting(new FloatSetting(
            "water_clarity_mult", "Underwater Clarity", "Multiplier for underwater view distance", 3.5f, 1.0f, 8.0f, 0.5f, "x"
    ));

    public ClearWater() {
        super(
                "clear_water",
                "Clear Water",
                "Improves underwater visibility and reduces murky fog.",
                ModuleCategory.VISUAL_GRAPHICS,
                false
        );
    }

    public float getClarityMultiplier() {
        return isEnabled() ? clarityMultiplier.getValue() : 1.0f;
    }
}
