package com.banglaclient.module.impl.coremod;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 306: BANGLACLIENT Icon System
 * Manages the procedural dual-tone vector icon renderer for all 315 modules, HUD badges, and nametags.
 */
public final class BanglaClientIconSystem extends AbstractModule {

    private final BooleanSetting dualToneRedAccents = registerSetting(new BooleanSetting(
            "dual_tone_red_accents", "Dual-Tone Red Highlights", "Render secondary crimson accents inside procedural module icons", true
    ));

    public BanglaClientIconSystem() {
        super(
                "banglaclient_icon_system",
                "BANGLACLIENT Icon System",
                "Provides procedural custom icons for all 315 modules and UI.",
                ModuleCategory.BANGLACLIENT_CORE,
                true
        );
    }

    public boolean isDualToneRedAccents() {
        return isEnabled() && dualToneRedAccents.isEnabled();
    }
}
