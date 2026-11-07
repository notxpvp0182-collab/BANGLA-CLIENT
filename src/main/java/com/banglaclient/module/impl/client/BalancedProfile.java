package com.banglaclient.module.impl.client;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.profile.ProfileManager;

/**
 * Module 266: Balanced Profile
 * Balances high FPS with crisp visual quality.
 */
public final class BalancedProfile extends AbstractModule {

    public BalancedProfile() {
        super(
                "balanced_profile",
                "Balanced Profile",
                "Balances high FPS with clean visual quality.",
                ModuleCategory.CLIENT_SETTINGS,
                false
        );
    }

    @Override
    public void onEnable() {
        ProfileManager.getInstance().applyProfile("balanced");
    }
}
