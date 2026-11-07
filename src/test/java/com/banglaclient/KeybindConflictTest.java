package com.banglaclient;

import com.banglaclient.keybind.KeyConflictDetector;
import com.banglaclient.keybind.Keybind;
import com.banglaclient.module.AllModulesBootstrap;
import com.banglaclient.module.Module;
import com.banglaclient.module.ModuleRegistry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.lwjgl.glfw.GLFW;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies that KeyConflictDetector accurately detects when two modules are bound to the same key.
 */
public class KeybindConflictTest {

    @BeforeAll
    static void init() {
        AllModulesBootstrap.registerAllModules();
    }

    @Test
    void verifyDuplicateKeybindDetection() {
        Module sprint = ModuleRegistry.getInstance().getById("toggle_sprint").orElseThrow();
        Module sneak = ModuleRegistry.getInstance().getById("toggle_sneak").orElseThrow();

        Keybind sharedKey = Keybind.ofKey(GLFW.GLFW_KEY_R);
        sprint.getKeybindSetting().orElseThrow().setValue(sharedKey);
        sneak.getKeybindSetting().orElseThrow().setValue(sharedKey);

        List<Module> conflicts = KeyConflictDetector.getInstance().findConflictsFor(sprint);
        assertTrue(conflicts.contains(sneak), "KeyConflictDetector must detect ToggleSneak sharing the same key as ToggleSprint");

        // Restore defaults
        sprint.resetToDefaults();
        sneak.resetToDefaults();
    }
}
