package com.banglaclient.module.impl.overlay;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.FloatSetting;

/**
 * Module 257: Custom Nametag
 * Customizes player nametag appearance, shadow text, scale, and third-person self-nametag visibility
 * (matching Screenshot 1's "PYES Player" / "BANGLA Player" module).
 */
public final class CustomNametag extends AbstractModule {

    private final BooleanSetting showOwnNametag = registerSetting(new BooleanSetting(
            "show_own_nametag", "Show Own Nametag In F5", "Render your own player nametag while in third-person perspective", true
    ));
    private final BooleanSetting textShadow = registerSetting(new BooleanSetting(
            "nametag_text_shadow", "Crisp Text Shadow", "Render drop shadows on 3D entity nametags", true
    ));
    private final FloatSetting nametagScale = registerSetting(new FloatSetting(
            "nametag_scale", "Nametag Scale", "Size multiplier for player nametags", 1.0f, 0.5f, 2.0f, 0.05f, "x"
    ));
    private final BooleanSetting transparentBackground = registerSetting(new BooleanSetting(
            "transparent_nametag_bg", "Transparent Plate", "Hide the dark rectangle behind nametags", false
    ));

    public CustomNametag() {
        super(
                "custom_nametag",
                "BANGLA Player",
                "Team name colors, labels, scale, and F5 nametag.",
                ModuleCategory.SCOREBOARD_TABLIST_NAMETAG,
                true
        );
    }

    public boolean isShowOwnNametag() { return isEnabled() && showOwnNametag.isEnabled(); }
    public boolean isTextShadow() { return isEnabled() && textShadow.isEnabled(); }
    public float getNametagScale() { return isEnabled() ? nametagScale.getValue() : 1.0f; }
    public boolean isTransparentBackground() { return isEnabled() && transparentBackground.isEnabled(); }
}
