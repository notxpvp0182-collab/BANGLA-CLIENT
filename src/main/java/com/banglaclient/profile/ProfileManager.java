package com.banglaclient.profile;

import com.banglaclient.config.ConfigManager;
import com.banglaclient.config.ConfigRecoveryHandler;
import com.banglaclient.core.BanglaLogger;
import com.banglaclient.core.ClientCore;
import com.banglaclient.module.Module;
import com.banglaclient.module.ModuleCategory;
import com.banglaclient.module.ModuleRegistry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Manages built-in and custom profiles:
 * Default, PvP, Survival, Low-End, Balanced, Quality, and Extreme FPS.
 */
public final class ProfileManager {

    private static final ProfileManager INSTANCE = new ProfileManager();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final Map<String, Profile> profiles = new LinkedHashMap<>();
    private final Path profilesDir;
    private Profile activeProfile;

    private ProfileManager() {
        this.profilesDir = ClientCore.getInstance().getConfigDirectory().resolve("profiles");
        registerBuiltInProfiles();
    }

    public static ProfileManager getInstance() {
        return INSTANCE;
    }

    private void registerBuiltInProfiles() {
        addBuiltIn("default", "Default");
        addBuiltIn("pvp", "PvP");
        addBuiltIn("survival", "Survival");
        addBuiltIn("low_end", "Low-End");
        addBuiltIn("balanced", "Balanced");
        addBuiltIn("quality", "Quality");
        addBuiltIn("extreme_fps", "Extreme FPS");
        this.activeProfile = profiles.get("default");
    }

    private void addBuiltIn(String id, String name) {
        profiles.put(id, new Profile(id, name, true, new JsonObject()));
    }

    public void applyProfile(String profileId) {
        if (profileId == null) {
            return;
        }
        String key = profileId.toLowerCase(Locale.ROOT);
        Profile profile = profiles.get(key);
        if (profile == null) {
            return;
        }

        // Save current profile before switching
        saveProfileToDisk(this.activeProfile);

        this.activeProfile = profile;
        ClientCore.getInstance().getClientState().setActiveProfileName(profile.getName());

        Path profileFile = profilesDir.resolve(profile.getId() + ".json");
        Optional<JsonObject> saved = ConfigRecoveryHandler.readWithRecovery(profileFile);

        if (saved.isPresent() && saved.get().has("modules")) {
            profile.setStateData(saved.get());
            ConfigManager.getInstance().applyJsonState(saved.get());
        } else if (profile.isBuiltIn()) {
            applyBuiltInPresetRules(profile.getId());
            saveProfileToDisk(profile);
        }

        ConfigManager.getInstance().saveConfig();
        BanglaLogger.info("Switched active profile to: {}", profile.getName());
    }

    private void applyBuiltInPresetRules(String id) {
        ModuleRegistry reg = ModuleRegistry.getInstance();
        switch (id) {
            case "pvp" -> {
                for (Module m : reg.getAllModules()) {
                    boolean enable = m.getCategory() == ModuleCategory.PVP_COMBAT
                            || m.getCategory() == ModuleCategory.OPTIMIZER
                            || m.getCategory() == ModuleCategory.SMALL_ITEM_SYSTEM
                            || m.getId().equals("toggle_sprint")
                            || m.getId().equals("fps_display")
                            || m.getId().equals("armor_hud")
                            || m.getId().equals("fullbright");
                    m.setEnabled(enable);
                }
            }
            case "survival" -> {
                for (Module m : reg.getAllModules()) {
                    boolean enable = m.getCategory() == ModuleCategory.WORLD_MAP
                            || m.getCategory() == ModuleCategory.INVENTORY_ITEMS
                            || m.getId().equals("coordinates_hud")
                            || m.getId().equals("direction_hud")
                            || m.getId().equals("biome_display")
                            || m.getId().equals("fps_display")
                            || m.getId().equals("entity_culling");
                    m.setEnabled(enable);
                }
            }
            case "low_end", "extreme_fps" -> {
                boolean extreme = id.equals("extreme_fps");
                for (Module m : reg.getAllModules()) {
                    if (m.getCategory() == ModuleCategory.OPTIMIZER
                            || m.getCategory() == ModuleCategory.RAM_CPU_GPU
                            || m.getCategory() == ModuleCategory.ADVANCED_PERFORMANCE) {
                        m.setEnabled(true);
                    } else if (extreme && m.getCategory() == ModuleCategory.VISUAL_GRAPHICS) {
                        m.setEnabled(m.getId().equals("no_fog") || m.getId().equals("fullbright"));
                    }
                }
            }
            case "balanced" -> {
                for (Module m : reg.getAllModules()) {
                    if (m.getCategory() == ModuleCategory.OPTIMIZER) {
                        m.setEnabled(true);
                    }
                }
            }
            case "quality" -> {
                for (Module m : reg.getAllModules()) {
                    if (m.getCategory() == ModuleCategory.VISUAL_GRAPHICS
                            || m.getCategory() == ModuleCategory.CLIENT_QUALITY_FEATURES) {
                        m.setEnabled(true);
                    }
                }
            }
            default -> {
                for (Module m : reg.getAllModules()) {
                    m.resetToDefaults();
                }
            }
        }
    }

    public Profile createCustomProfile(String displayName) {
        if (displayName == null || displayName.isBlank()) {
            return activeProfile;
        }
        String cleanName = displayName.trim();
        String id = cleanName.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_]", "_");
        if (profiles.containsKey(id)) {
            return profiles.get(id);
        }
        JsonObject snapshot = ConfigManager.getInstance().serializeCurrentStateToJson();
        Profile created = new Profile(id, cleanName, false, snapshot);
        profiles.put(id, created);
        saveProfileToDisk(created);
        return created;
    }

    public boolean deleteCustomProfile(String profileId) {
        Profile p = profiles.get(profileId);
        if (p == null || p.isBuiltIn()) {
            return false;
        }
        profiles.remove(profileId);
        try {
            Files.deleteIfExists(profilesDir.resolve(profileId + ".json"));
        } catch (IOException ignored) {
        }
        if (activeProfile == p) {
            applyProfile("default");
        }
        return true;
    }

    public void saveProfileToDisk(Profile profile) {
        if (profile == null) {
            return;
        }
        try {
            JsonObject current = ConfigManager.getInstance().serializeCurrentStateToJson();
            profile.setStateData(current);
            Path path = profilesDir.resolve(profile.getId() + ".json");
            ConfigRecoveryHandler.writeAtomically(path, GSON.toJson(current));
        } catch (IOException e) {
            BanglaLogger.error("Failed to save profile {}", profile.getName(), e);
        }
    }

    public Profile getActiveProfile() {
        return activeProfile;
    }

    public List<Profile> getAllProfiles() {
        return Collections.unmodifiableList(new ArrayList<>(profiles.values()));
    }
}
