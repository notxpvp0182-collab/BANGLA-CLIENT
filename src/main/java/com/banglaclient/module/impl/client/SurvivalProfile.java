package com.banglaclient.module.impl.client;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.profile.ProfileManager;

/**
 * Module 264: Survival Profile
 * One-click module card to activate the Survival-oriented configuration profile.
 */
public final class SurvivalProfile extends AbstractModule {

    public SurvivalProfile() {
        super(
                "survival_profile",
                "Survival Profile",
                "Applies survival-oriented coordinates, waypoints, and inventory configuration.",
                ModuleCategory.CLIENT_SETTINGS,
                false
        );
    }

    @Override
    public void onEnable() {
        ProfileManager.getInstance().applyProfile("survival");
    }
}
