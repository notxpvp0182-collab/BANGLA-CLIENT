package com.banglaclient.module.impl.client;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.profile.ProfileManager;

/**
 * Module 263: PvP Profile
 * One-click module card to activate the PvP-oriented configuration profile.
 */
public final class PvpProfile extends AbstractModule {

    public PvpProfile() {
        super(
                "pvp_profile",
                "PvP Profile",
                "Applies PvP-oriented HUD, small-item, and combat configuration.",
                ModuleCategory.CLIENT_SETTINGS,
                false
        );
    }

    @Override
    public void onEnable() {
        ProfileManager.getInstance().applyProfile("pvp");
    }
}
