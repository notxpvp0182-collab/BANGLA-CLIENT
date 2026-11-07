package com.banglaclient.module.impl.worldvisual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 191: Lava Quality Control
 * Controls lava visual quality, ambient lava sound/particle clutter, and Nether fog contrast.
 */
public final class LavaQualityControl extends AbstractModule {

    private final BooleanSetting disableLavaDripParticles = registerSetting(new BooleanSetting(
            "disable_lava_drips", "Disable Ceiling Drips", "Hide ceiling lava drip particles in caves and the Nether", true
    ));
    private final BooleanSetting reduceLavaOverlayOpacity = registerSetting(new BooleanSetting(
            "reduce_lava_overlay", "Low Lava Screen Tint", "Reduce orange screen tint when touching lava", true
    ));

    public LavaQualityControl() {
        super(
                "lava_quality_control",
                "Lava Quality Control",
                "Controls lava visual quality, drips, and screen tint.",
                ModuleCategory.WORLD_VISUAL,
                false
        );
    }

    public boolean isDisableLavaDripParticles() { return isEnabled() && disableLavaDripParticles.isEnabled(); }
    public boolean isReduceLavaOverlayOpacity() { return isEnabled() && reduceLavaOverlayOpacity.isEnabled(); }
}
