package com.banglaclient.module.impl.quality;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 294: Custom Main Menu
 * Controls the BANGLACLIENT Main Menu replacement (Screenshot 2 design reference, without Store button).
 */
public final class CustomMainMenuModule extends AbstractModule {

    private final BooleanSetting showExternalModButtons = registerSetting(new BooleanSetting(
            "show_external_mod_buttons", "Show External Mod Sidebar", "Display detected external mod buttons on the left side of the Main Menu", true
    ));
    private final BooleanSetting showBanglaSmpCard = registerSetting(new BooleanSetting(
            "show_bangla_smp_card", "Show Bangla SMP Info", "Display play.banglaSMP.net server info card in bottom-right corner", true
    ));

    public CustomMainMenuModule() {
        super(
                "custom_main_menu",
                "Custom Main Menu",
                "Provides the custom BANGLACLIENT Main Menu.",
                ModuleCategory.CLIENT_QUALITY_FEATURES,
                true
        );
    }

    public boolean isShowExternalModButtons() { return isEnabled() && showExternalModButtons.isEnabled(); }
    public boolean isShowBanglaSmpCard() { return isEnabled() && showBanglaSmpCard.isEnabled(); }
}
