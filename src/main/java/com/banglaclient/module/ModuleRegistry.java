package com.banglaclient.module;

import com.banglaclient.core.BanglaLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Central registry managing all 315 BANGLACLIENT modules.
 * Provides fast lookup by ID, category filtering, live search, HUD filtering,
 * and keybind-capable module discovery without reflection or per-frame allocations.
 */
public final class ModuleRegistry {

    private static final ModuleRegistry INSTANCE = new ModuleRegistry();

    private final Map<String, Module> modulesById = new LinkedHashMap<>();
    private final List<Module> allModules = new ArrayList<>(320);
    private final Map<ModuleCategory, List<Module>> modulesByCategory = new EnumMap<>(ModuleCategory.class);
    private final List<HudModule> hudModules = new ArrayList<>(64);

    private ModuleRegistry() {
        for (ModuleCategory category : ModuleCategory.values()) {
            modulesByCategory.put(category, new ArrayList<>());
        }
    }

    public static ModuleRegistry getInstance() {
        return INSTANCE;
    }

    public synchronized void register(Module module) {
        if (module == null) {
            return;
        }
        String normalizedId = module.getId().toLowerCase(Locale.ROOT);
        if (modulesById.containsKey(normalizedId)) {
            BanglaLogger.warn("Duplicate module ID skipped during registration: {}", normalizedId);
            return;
        }
        modulesById.put(normalizedId, module);
        allModules.add(module);
        modulesByCategory.get(module.getCategory()).add(module);

        if (module instanceof HudModule hudModule) {
            hudModules.add(hudModule);
        }
    }

    public Optional<Module> getById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(modulesById.get(id.toLowerCase(Locale.ROOT)));
    }

    @SuppressWarnings("unchecked")
    public <M extends Module> Optional<M> getByClass(Class<M> moduleClass) {
        for (int i = 0, size = allModules.size(); i < size; i++) {
            Module m = allModules.get(i);
            if (moduleClass.isInstance(m)) {
                return Optional.of((M) m);
            }
        }
        return Optional.empty();
    }

    public List<Module> getAllModules() {
        return Collections.unmodifiableList(allModules);
    }

    public List<Module> getByCategory(ModuleCategory category) {
        List<Module> list = modulesByCategory.get(category);
        return list != null ? Collections.unmodifiableList(list) : Collections.emptyList();
    }

    public List<HudModule> getAllHudModules() {
        return Collections.unmodifiableList(hudModules);
    }

    /**
     * Returns only currently enabled HUD modules for rendering and the HUD Editor.
     */
    public List<HudModule> getEnabledHudModules() {
        List<HudModule> enabled = new ArrayList<>();
        for (int i = 0, size = hudModules.size(); i < size; i++) {
            HudModule hud = hudModules.get(i);
            if (hud.isEnabled()) {
                enabled.add(hud);
            }
        }
        return enabled;
    }

    public List<Module> getKeybindCapableModules() {
        List<Module> list = new ArrayList<>();
        for (int i = 0, size = allModules.size(); i < size; i++) {
            Module m = allModules.get(i);
            if (m.hasKeybind()) {
                list.add(m);
            }
        }
        return list;
    }

    public List<Module> search(String query) {
        if (query == null || query.isBlank()) {
            return getAllModules();
        }
        String q = query.trim().toLowerCase(Locale.ROOT);
        List<Module> results = new ArrayList<>();
        for (int i = 0, size = allModules.size(); i < size; i++) {
            Module m = allModules.get(i);
            if (m.getName().toLowerCase(Locale.ROOT).contains(q)
                    || m.getDescription().toLowerCase(Locale.ROOT).contains(q)
                    || m.getCategory().getDisplayName().toLowerCase(Locale.ROOT).contains(q)) {
                results.add(m);
            }
        }
        return results;
    }

    public int getEnabledCount() {
        int count = 0;
        for (int i = 0, size = allModules.size(); i < size; i++) {
            if (allModules.get(i).isEnabled()) {
                count++;
            }
        }
        return count;
    }

    public int getActiveOptimizationCount() {
        int count = 0;
        for (int i = 0, size = allModules.size(); i < size; i++) {
            Module m = allModules.get(i);
            if (m.isEnabled() && (m.getCategory() == ModuleCategory.OPTIMIZER
                    || m.getCategory() == ModuleCategory.PERFORMANCE_FPS
                    || m.getCategory() == ModuleCategory.RAM_CPU_GPU
                    || m.getCategory() == ModuleCategory.ADVANCED_PERFORMANCE)) {
                count++;
            }
        }
        return count;
    }

    public void clearForTesting() {
        modulesById.clear();
        allModules.clear();
        hudModules.clear();
        for (List<Module> list : modulesByCategory.values()) {
            list.clear();
        }
    }
}
