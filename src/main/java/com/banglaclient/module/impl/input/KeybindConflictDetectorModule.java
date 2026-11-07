package com.banglaclient.module.impl.input;

import com.banglaclient.keybind.KeyConflictDetector;
import com.banglaclient.keybind.Keybind;
import com.banglaclient.module.AbstractModule;
import com.banglaclient.module.Module;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.setting.BooleanSetting;

import java.util.List;
import java.util.Map;

/**
 * Module 089: Keybind Conflict Detector
 * Detects duplicate/conflicting bindings and warns the user in module settings panels.
 */
public final class KeybindConflictDetectorModule extends AbstractModule {

    private final BooleanSetting highlightInSettings = registerSetting(new BooleanSetting(
            "highlight_in_settings", "Show Warning Badge", "Display a warning icon when two modules share the same key", true
    ));

    public KeybindConflictDetectorModule() {
        super(
                "keybind_conflict_detector",
                "Keybind Conflict Detector",
                "Detects duplicate or conflicting module keybindings.",
                ModuleCategory.KEYBINDS_INPUT_MOVEMENT,
                true
        );
    }

    public int getActiveConflictCount() {
        if (!isEnabled()) {
            return 0;
        }
        Map<Keybind, List<Module>> conflicts = KeyConflictDetector.getInstance().findAllConflicts();
        return conflicts.size();
    }

    public boolean isHighlightInSettings() {
        return isEnabled() && highlightInSettings.isEnabled();
    }
}
