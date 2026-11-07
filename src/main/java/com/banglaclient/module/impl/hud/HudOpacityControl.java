package com.banglaclient.module.impl.hud;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.HudModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.setting.ActionSetting;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 125: HUD Opacity Control
 * Allows adjusting overall HUD element opacity and applying it across active HUD overlays.
 */
public final class HudOpacityControl extends AbstractModule {

    private final FloatSetting masterOpacity = registerSetting(new FloatSetting(
            "master_hud_opacity", "Global HUD Opacity", "Opacity level applied when clicking Apply Opacity", 1.0f, 0.2f, 1.0f, 0.05f, ""
    ));

    public HudOpacityControl() {
        super(
                "hud_opacity_control",
                "HUD Opacity Control",
                "Allows individual and global HUD opacity control.",
                ModuleCategory.HUD_INFORMATION,
                true
        );

        registerSetting(new ActionSetting(
                "apply_opacity_all",
                "Apply To All HUDs",
                "Set all HUD elements to the selected opacity value",
                "Apply Opacity",
                () -> {
                    float op = masterOpacity.getValue();
                    for (HudModule hud : ModuleRegistry.getInstance().getAllHudModules()) {
                        hud.getHudStyle().setOpacity(op);
                    }
                }
        ));
    }
}
