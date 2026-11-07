package com.banglaclient;

import com.banglaclient.module.AllModulesBootstrap;
import com.banglaclient.module.Module;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.profile.Profile;
import com.banglaclient.profile.ProfileManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies built-in profile registration and that switching profiles updates the active profile.
 */
public class ProfileSwitchTest {

    @BeforeAll
    static void setup() {
        AllModulesBootstrap.registerAllModules();
    }

    @AfterAll
    static void restoreDefault() {
        ProfileManager.getInstance().applyProfile("default");
    }

    @Test
    void verifyBuiltInProfilesExist() {
        ProfileManager manager = ProfileManager.getInstance();
        for (String id : new String[]{"default", "pvp", "survival", "low_end", "balanced", "quality", "extreme_fps"}) {
            boolean found = manager.getAllProfiles().stream().anyMatch(p -> p.getId().equals(id) && p.isBuiltIn());
            assertTrue(found, "Missing built-in profile: " + id);
        }
    }

    @Test
    void verifySwitchingProfileChangesActiveProfile() {
        ProfileManager manager = ProfileManager.getInstance();

        manager.applyProfile("pvp");
        assertEquals("pvp", manager.getActiveProfile().getId());

        manager.applyProfile("survival");
        assertEquals("survival", manager.getActiveProfile().getId());

        manager.applyProfile("does_not_exist");
        assertEquals("survival", manager.getActiveProfile().getId(), "Unknown profile ids must be ignored");
    }

    @Test
    void verifyCustomProfileCreateAndDelete() {
        ProfileManager manager = ProfileManager.getInstance();
        Profile created = manager.createCustomProfile("Unit Test Profile");
        assertNotNull(created);
        assertFalse(created.isBuiltIn());
        assertTrue(manager.deleteCustomProfile(created.getId()));
        assertFalse(manager.deleteCustomProfile("default"), "Built-in profiles must not be deletable");
    }
}
