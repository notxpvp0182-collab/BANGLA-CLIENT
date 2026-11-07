package com.banglaclient.module.impl.client;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.profile.ProfileManager;
import com.banglaclient.setting.ActionSetting;

/**
 * Module 260: Profile Manager
 * Creates, saves, and manages BANGLACLIENT configuration profiles.
 */
public final class ProfileManagerModule extends AbstractModule {

    public ProfileManagerModule() {
        super(
                "profile_manager",
                "Profile Manager",
                "Creates and manages configuration profiles.",
                ModuleCategory.CLIENT_SETTINGS,
                true
        );

        registerSetting(new ActionSetting(
                "save_current_profile",
                "Save Active Profile",
                "Save current module states and HUD positions to the active profile",
                "Save Profile Now",
                () -> ProfileManager.getInstance().saveProfileToDisk(ProfileManager.getInstance().getActiveProfile())
        ));
    }
}
