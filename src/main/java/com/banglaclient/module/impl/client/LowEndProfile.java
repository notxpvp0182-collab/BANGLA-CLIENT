package com.banglaclient.module.impl.client;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.profile.ProfileManager;

/**
 * Module 265: Low-End Profile
 * Optimizes client settings specifically for low-end laptops and integrated GPUs.
 */
public final class LowEndProfile extends AbstractModule {

    public LowEndProfile() {
        super(
                "low_end_profile",
                "Low-End Profile",
                "Optimizes settings for low-end CPUs and integrated GPUs.",
                ModuleCategory.CLIENT_SETTINGS,
                false
        );
    }

    @Override
    public void onEnable() {
        ProfileManager.getInstance().applyProfile("low_end");
    }
}
