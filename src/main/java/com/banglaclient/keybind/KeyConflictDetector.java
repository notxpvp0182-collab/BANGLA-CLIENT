package com.banglaclient.keybind;

import com.banglaclient.module.Module;
import com.banglaclient.module.ModuleRegistry;
import com.banglaclient.setting.KeybindSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Detects duplicate or conflicting keybindings across all registered BANGLACLIENT modules.
 */
public final class KeyConflictDetector {

    private static final KeyConflictDetector INSTANCE = new KeyConflictDetector();

    private KeyConflictDetector() {
    }

    public static KeyConflictDetector getInstance() {
        return INSTANCE;
    }

    /**
     * Returns all modules bound to the same keybind as the given module (excluding itself).
     */
    public List<Module> findConflictsFor(Module targetModule) {
        if (targetModule == null || !targetModule.hasKeybind()) {
            return Collections.emptyList();
        }
        Optional<KeybindSetting> optSetting = targetModule.getKeybindSetting();
        if (optSetting.isEmpty() || !optSetting.get().isBound()) {
            return Collections.emptyList();
        }
        Keybind targetKey = optSetting.get().getValue();
        List<Module> conflicts = new ArrayList<>();

        for (Module other : ModuleRegistry.getInstance().getKeybindCapableModules()) {
            if (other == targetModule) {
                continue;
            }
            Optional<KeybindSetting> otherSetting = other.getKeybindSetting();
            if (otherSetting.isPresent() && otherSetting.get().isBound()) {
                if (targetKey.equals(otherSetting.get().getValue())) {
                    conflicts.add(other);
                }
            }
        }
        return conflicts;
    }

    /**
     * Scans all modules and returns a map of Keybind -> List of conflicting Modules (size >= 2).
     */
    public Map<Keybind, List<Module>> findAllConflicts() {
        Map<Keybind, List<Module>> grouped = new HashMap<>();
        for (Module m : ModuleRegistry.getInstance().getKeybindCapableModules()) {
            m.getKeybindSetting().ifPresent(setting -> {
                if (setting.isBound()) {
                    grouped.computeIfAbsent(setting.getValue(), k -> new ArrayList<>()).add(m);
                }
            });
        }
        Map<Keybind, List<Module>> conflictsOnly = new HashMap<>();
        for (Map.Entry<Keybind, List<Module>> entry : grouped.entrySet()) {
            if (entry.getValue().size() > 1) {
                conflictsOnly.put(entry.getKey(), entry.getValue());
            }
        }
        return conflictsOnly;
    }
}
