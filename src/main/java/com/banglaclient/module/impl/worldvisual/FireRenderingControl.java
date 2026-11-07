package com.banglaclient.module.impl.worldvisual;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.ModeSetting;

/**
 * Module 192: Fire Rendering Control
 * Controls world fire block and soul-fire visual appearance and smoke output.
 */
public final class FireRenderingControl extends AbstractModule {

    private final ModeSetting fireSmokeMode = registerSetting(new ModeSetting(
            "fire_smoke_mode", "Fire Smoke", "World fire block smoke behavior", "Minimal", "All", "Minimal", "None"
    ));
    private final BooleanSetting hideFirstPersonOnCreative = registerSetting(new BooleanSetting(
            "hide_fp_fire_creative", "Hide Creative Fire Overlay", "Never render first-person fire overlay while in Creative mode", true
    ));

    public FireRenderingControl() {
        super(
                "fire_rendering_control",
                "Fire Rendering Control",
                "Controls world fire smoke and Creative fire overlay.",
                ModuleCategory.WORLD_VISUAL,
                false
        );
    }

    public String getFireSmokeMode() { return fireSmokeMode.getValue(); }
    public boolean isHideFirstPersonOnCreative() { return isEnabled() && hideFirstPersonOnCreative.isEnabled(); }
}
