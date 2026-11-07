package com.banglaclient.module.impl.client;

import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.profile.ProfileManager;

/**
 * Module 267: Quality Profile
 * Prioritizes rich visual quality while keeping non-intrusive client optimizations active.
 */
public final class QualityProfile extends AbstractModule {

    public QualityProfile() {
        super(
                "quality_profile",
                "Quality Profile",
                "Prioritizes visual quality and smooth animations.",
                ModuleCategory.CLIENT_SETTINGS,
                false
        );
    }

    @Override
    public void onEnable() {
        ProfileManager.getInstance().applyProfile("quality");
    }
}
