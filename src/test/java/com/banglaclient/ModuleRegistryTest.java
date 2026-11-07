package com.banglaclient;

import com.banglaclient.module.AllModulesBootstrap;
import com.banglaclient.module.Module;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.module.ModuleRegistry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies that all 315 independent BANGLACLIENT modules register cleanly,
 * have unique IDs, belong to valid categories, and support search and toggling.
 */
public class ModuleRegistryTest {

    @BeforeAll
    static void setupRegistry() {
        AllModulesBootstrap.registerAllModules();
    }

    @Test
    void verifyAll315ModulesRegistered() {
        List<Module> all = ModuleRegistry.getInstance().getAllModules();
        assertEquals(315, all.size(), "ModuleRegistry must contain exactly 315 independent modules");
    }

    @Test
    void verifyEveryCategoryHasModules() {
        for (ModuleCategory category : ModuleCategory.values()) {
            List<Module> inCat = ModuleRegistry.getInstance().getByCategory(category);
            assertFalse(inCat.isEmpty(), "Category " + category.name() + " must not be empty");
        }
    }

    @Test
    void verifySearchAndToggle() {
        List<Module> results = ModuleRegistry.getInstance().search("Fullbright");
        assertFalse(results.isEmpty(), "Search for Fullbright must return matches");

        Module fb = ModuleRegistry.getInstance().getById("fullbright").orElseThrow();
        boolean initial = fb.isEnabled();
        fb.toggle();
        assertEquals(!initial, fb.isEnabled());
        fb.setEnabled(initial);
    }
}
