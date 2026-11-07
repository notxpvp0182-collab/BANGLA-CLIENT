package com.banglaclient.module.impl.coremod;

import com.banglaclient.keybind.Keybind;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.profile.Profile;
import com.banglaclient.profile.ProfileManager;
import org.lwjgl.glfw.GLFW;

import java.util.List;

/**
 * Module 310: Profile Switching
 * Allows cycling through saved profiles quickly via a configurable keybind or module click.
 */
public final class ProfileSwitching extends AbstractModule {

    public ProfileSwitching() {
        super(
                "profile_switching",
                "Profile Switching",
                "Cycles between saved configuration profiles quickly.",
                ModuleCategory.BANGLACLIENT_CORE,
                false
        );
        registerKeybind(Keybind.ofKey(GLFW.GLFW_KEY_UNKNOWN));
    }

    @Override
    public void onEnable() {
        cycleToNextProfile();
        setEnabled(false);
    }

    public void cycleToNextProfile() {
        List<Profile> list = ProfileManager.getInstance().getAllProfiles();
        if (list.isEmpty()) return;
        Profile current = ProfileManager.getInstance().getActiveProfile();
        int idx = list.indexOf(current);
        int nextIdx = (idx + 1) % list.size();
        ProfileManager.getInstance().applyProfile(list.get(nextIdx).getId());
    }
}
