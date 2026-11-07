package com.banglaclient.module.impl.hud;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.setting.ActionSetting;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 124: HUD Scale Control
 * Allows applying a uniform scale multiplier across all HUD elements or resetting individual HUD scales.
 */
public final class HudScaleControl extends AbstractModule {

    private final FloatSetting masterScale = registerSetting(new FloatSetting(
            "master_hud_scale", "Global HUD Scale", "Scale multiplier applied when clicking Apply Scale", 1.0f, 0.5f, 2.5f, 0.05f, "x"
    ));

    public HudScaleControl() {
        super(
                "hud_scale_control",
                "HUD Scale Control",
                "Controls individual and global HUD element scaling.",
                ModuleCategory.HUD_INFORMATION,
                true
        );

        registerSetting(new ActionSetting(
                "apply_scale_all",
                "Apply To All HUDs",
                "Set all HUD elements to the selected scale multiplier",
                "Apply Scale",
                () -> {
                    float s = masterScale.getValue();
                    for (HudModule hud : ModuleRegistry.getInstance().getAllHudModules()) {
                        hud.getHudStyle().setScale(s);
                    }
                }
        ));
    }
}
