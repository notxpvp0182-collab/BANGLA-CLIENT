package com.banglaclient.module.impl.overlay;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;
import com.banglaclient.setting.ModeSetting;

/**
 * Module 259: BC Nametag Icon
 * Displays a crimson/white BANGLACLIENT `[BC]` badge/icon beside your nametag and compatible players.
 */
public final class BcNametagIcon extends AbstractModule {

    private final ModeSetting badgeStyle = registerSetting(new ModeSetting(
            "bc_badge_style", "Badge Style", "Visual style of the BANGLACLIENT nametag indicator",
            "Red [BC] Tag", "Red [BC] Tag", "Diamond Crest", "Minimal Dot"
    ));
    private final BooleanSetting showInTablist = registerSetting(new BooleanSetting(
            "show_in_tablist", "Show In Tablist", "Also display the BC indicator next to your name in the tablist", true
    ));

    public BcNametagIcon() {
        super(
                "bc_nametag_icon",
                "BC Nametag Icon",
                "Displays a BANGLACLIENT icon/badge with nametags.",
                ModuleCategory.SCOREBOARD_TABLIST_NAMETAG,
                true
        );
    }

    public String getFormattedPrefix() {
        if (!isEnabled()) {
            return "";
        }
        return switch (badgeStyle.getValue()) {
            case "Diamond Crest" -> "§c◆ §r";
            case "Minimal Dot" -> "§c• §r";
            default -> "§8[§cBC§8] §r";
        };
    }

    public boolean isShowInTablist() {
        return isEnabled() && showInTablist.isEnabled();
    }
}
