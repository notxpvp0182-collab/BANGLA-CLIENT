package com.banglaclient.module.impl.client;

import com.banglaclient.core.ClientCore;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;

/**
 * Module 276: Safe Mode
 * Runs the client with aggressive low-level culling hooks disabled for troubleshooting.
 */
public final class SafeMode extends AbstractModule {

    public SafeMode() {
        super(
                "safe_mode",
                "Safe Mode",
                "Runs the client with aggressive hooks disabled for stability.",
                ModuleCategory.CLIENT_SETTINGS,
                false
        );
    }

    @Override
    public void onEnable() {
        ClientCore.getInstance().getClientState().setSafeMode(true);
    }

    @Override
    public void onDisable() {
        ClientCore.getInstance().getClientState().setSafeMode(false);
    }
}
