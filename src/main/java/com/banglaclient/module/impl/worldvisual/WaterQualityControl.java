package com.banglaclient.module.impl.worldvisual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.IntegerSetting;

/**
 * Module 190: Water Quality Control
 * Controls biome water color blending radius and surface quality for performance vs aesthetics.
 */
public final class WaterQualityControl extends AbstractModule {

    private final IntegerSetting biomeBlendRadius = registerSetting(new IntegerSetting(
            "water_biome_blend", "Biome Blend Radius", "Biome color transition radius (0 = Fastest, 5 = Smooth)", 2, 0, 7, 1, ""
    ));
    private final BooleanSetting disableUnderwaterOverlayTexture = registerSetting(new BooleanSetting(
            "disable_underwater_tex", "Hide Underwater Vignette", "Remove the dark underwater.png screen vignette", true
    ));

    public WaterQualityControl() {
        super(
                "water_quality_control",
                "Water Quality Control",
                "Controls water biome blending and underwater vignette.",
                ModuleCategory.WORLD_VISUAL,
                false
        );
        biomeBlendRadius.onChange((oldV, newV) -> {
            if (isEnabled() && mc.options != null) {
                mc.options.getBiomeBlendRadius().setValue(newV);
            }
        });
    }

    @Override
    public void onEnable() {
        if (mc.options != null) {
            mc.options.getBiomeBlendRadius().setValue(biomeBlendRadius.getValue());
        }
    }

    public boolean isDisableUnderwaterOverlayTexture() {
        return isEnabled() && disableUnderwaterOverlayTexture.isEnabled();
    }
}
