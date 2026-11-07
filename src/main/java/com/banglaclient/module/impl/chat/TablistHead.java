package com.banglaclient.module.impl.chat;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

/**
 * Module 221: Tablist Head
 * Ensures player skin heads and 3D hat layers are rendered in the multiplayer tablist even on offline-mode/proxy servers.
 */
public final class TablistHead extends AbstractModule {

    private final BooleanSetting forceShowHeads = registerSetting(new BooleanSetting(
            "force_show_heads", "Force Tablist Heads", "Always render 8x8 player heads in the tablist", true
    ));
    private final BooleanSetting showOuterHat = registerSetting(new BooleanSetting(
            "show_outer_hat", "Show Hat Layer", "Render 3D skin hat layer in tablist entries", true
    ));

    public TablistHead() {
        super(
                "tablist_head",
                "Tablist Head",
                "Shows player heads and hat layers in the tablist.",
                ModuleCategory.CHAT,
                true
        );
    }

    public boolean isForceShowHeads() { return isEnabled() && forceShowHeads.isEnabled(); }
    public boolean isShowOuterHat() { return isEnabled() && showOuterHat.isEnabled(); }
}
