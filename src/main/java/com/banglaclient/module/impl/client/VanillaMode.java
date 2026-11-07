package com.banglaclient.module.impl.client;

import com.banglaclient.core.ClientCore;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;

/**
 * Module 279: Vanilla Mode
 * Temporarily suspends BANGLACLIENT overlays and gameplay hooks for a near-vanilla experience
 * without losing your saved configuration.
 */
public final class VanillaMode extends AbstractModule {

    public VanillaMode() {
        super(
                "vanilla_mode",
                "Vanilla Mode",
                "Temporarily suspends BANGLACLIENT features for a vanilla session.",
                ModuleCategory.CLIENT_SETTINGS,
                false
        );
    }

    @Override
    public void onEnable() {
        ClientCore.getInstance().getClientState().setVanillaMode(true);
    }

    @Override
    public void onDisable() {
        ClientCore.getInstance().getClientState().setVanillaMode(false);
    }
}
