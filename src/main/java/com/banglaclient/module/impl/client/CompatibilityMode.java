package com.banglaclient.module.impl.client;

import com.banglaclient.core.ClientCore;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;

/**
 * Module 277: Compatibility Mode
 * Uses conservative rendering hooks when running alongside third-party shader or rendering mods.
 */
public final class CompatibilityMode extends AbstractModule {

    public CompatibilityMode() {
        super(
                "compatibility_mode",
                "Compatibility Mode",
                "Uses conservative rendering hooks alongside external shader mods.",
                ModuleCategory.CLIENT_SETTINGS,
                false
        );
    }

    @Override
    public void onEnable() {
        ClientCore.getInstance().getClientState().setCompatibilityMode(true);
    }

    @Override
    public void onDisable() {
        ClientCore.getInstance().getClientState().setCompatibilityMode(false);
    }
}
