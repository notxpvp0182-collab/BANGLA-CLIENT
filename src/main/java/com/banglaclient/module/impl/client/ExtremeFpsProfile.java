package com.banglaclient.module.impl.client;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.profile.ProfileManager;

/**
 * Module 268: Extreme FPS Profile
 * Aggressively reduces visual and particle workload for maximum competitive frame rates.
 */
public final class ExtremeFpsProfile extends AbstractModule {

    public ExtremeFpsProfile() {
        super(
                "extreme_fps_profile",
                "Extreme FPS Profile",
                "Aggressively reduces visual workload for maximum FPS.",
                ModuleCategory.CLIENT_SETTINGS,
                false
        );
    }

    @Override
    public void onEnable() {
        ProfileManager.getInstance().applyProfile("extreme_fps");
    }
}
